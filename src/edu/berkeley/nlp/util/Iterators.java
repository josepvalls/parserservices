package edu.berkeley.nlp.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;

public class Iterators {

	public static <T> void fillList(Iterator<? extends T> it, List<T> lst) {
		while (it.hasNext()) {
			lst.add(it.next());
		}
	}

	public static <T> List<T> fillList(Iterator<? extends T> it) {
		List<T> lst = new ArrayList<T>();
		fillList(it, lst);
		return lst;
	}

	/**
	 * Wraps a base iterator with a transformation function.
	 */
	public static abstract class Transform<S, T> implements Iterator<T> {

		private Iterator<S> base;

		public Transform(Iterator<S> base) {
			this.base = base;
		}

		@Override
		public boolean hasNext() {
			return base.hasNext();
		}

		@Override
		public T next() {
			return transform(base.next());
		}

		protected abstract T transform(S next);

		@Override
		public void remove() {
			base.remove();
		}

	}

	private Iterators() {
	}

	/**
	 * Wraps an iterator as an iterable
	 * 
	 * @param <T>
	 * @param it
	 * @return
	 */
	public static <T> Iterable<T> newIterable(final Iterator<T> it) {
		return new Iterable<T>() {
			@Override
			public Iterator<T> iterator() {
				return it;
			}
		};
	}

	/**
	 * Wraps an iterator as an iterable
	 * 
	 * @param <T>
	 * @param it
	 * @return
	 */
	public static <T> Iterable<T> able(final Iterator<T> it) {
		return new Iterable<T>() {
			boolean used = false;

			@Override
			public Iterator<T> iterator() {
				if (used)
					throw new RuntimeException("One use iterable");
				used = true;
				return it;
			}
		};
	}

	/**
	 * Executes calls to next() in a different thread
	 * 
	 * @param <T>
	 * @param base
	 * @param numThreads
	 * @return
	 */
	public static <T> Iterator<T> thread(final Iterator<T> base) {
		return new Iterator<T>() {
			ArrayBlockingQueue<T> els = new ArrayBlockingQueue<T>(2);
			private boolean finishedLoading = false;
			private boolean running = false;

			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					while (base.hasNext()) {
						try {
							els.put(base.next());
						} catch (InterruptedException e) {
							throw new RuntimeException(e);
						}
					}
					finishedLoading = true;
				}
			});

			@Override
			public boolean hasNext() {
				return !(finishedLoading && els.isEmpty());
			}

			@Override
			public T next() {
				if (!running)
					thread.start();
				running = true;
				try {
					return els.take();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public static <S, T> Iterator<Pair<S, T>> zip(final Iterator<S> s,
			final Iterator<T> t) {
		return new Iterator<Pair<S, T>>() {
			@Override
			public boolean hasNext() {
				return s.hasNext() && t.hasNext();
			}

			@Override
			public Pair<S, T> next() {
				return Pair.newPair(s.next(), t.next());
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	/**
	 * Provides a max number of elements for an underlying base iterator.
	 */
	public static <T> Iterator<T> maxLengthIterator(final Iterator<T> base,
			final int max) {
		return new Iterator<T>() {
			int count = 0;

			@Override
			public boolean hasNext() {
				return base.hasNext() && count < max;
			}

			@Override
			public T next() {
				if (!hasNext())
					throw new NoSuchElementException("No more elements");
				count++;
				return base.next();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
				// TODO Maybe this should behave in a more friendly manner
			}

		};
	}

	/**
	 * Wraps a two-level iteration scenario in an iterator. Each key of the keys
	 * iterator returns an iterator (via the factory) over T's.
	 * 
	 * The IteratorIterator loops through the iterator associated with each key
	 * until all the keys are used up.
	 */
	public static class IteratorIterator<T> implements Iterator<T> {
		Iterator<T> current = null;
		Iterator keys;
		Factory<Iterator<T>> iterFactory;

		public IteratorIterator(Iterator keys, Factory<Iterator<T>> iterFactory) {
			this.keys = keys;
			this.iterFactory = iterFactory;
			current = getNextIterator();
		}

		private Iterator<T> getNextIterator() {
			Iterator<T> next = null;
			while (next == null) {
				if (!keys.hasNext())
					break;
				next = iterFactory.newInstance(keys.next());
				if (!next.hasNext())
					next = null;
			}
			return next;
		}

		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public T next() {
			T next = current.next();
			if (!current.hasNext())
				current = getNextIterator();
			return next;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	/**
	 * Creates an iterator that only returns items of a base iterator that pass
	 * a filter.
	 * 
	 * Null items cannot be returned from the base iterator.
	 */
	public static class FilteredIterator<T> implements Iterator<T> {
		Filter<T> filter;
		T next;
		private Iterator<T> base;

		public FilteredIterator(Filter<T> filter, Iterator<T> base) {
			super();
			this.filter = filter;
			this.base = base;
			loadNext();
		}

		public FilteredIterator(Filter<T> filter, Iterable<T> items) {
			this(filter, items.iterator());
		}

		private void loadNext() {
			next = null;
			while (next == null && base.hasNext()) {
				next = base.next();
				if (!filter.accept(next))
					next = null;
			}
		}

		@Override
		public boolean hasNext() {
			return next != null;
		}

		@Override
		public T next() {
			T old = next;
			loadNext();
			return old;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	public static class TransformingIterator<I, O> implements Iterator<O> {
		private MyMethod<I, O> transformer;
		private Iterator<I> inputIterator;

		public TransformingIterator(Iterator<I> inputIterator,
				MyMethod<I, O> transformer) {
			this.inputIterator = inputIterator;
			this.transformer = transformer;
		}

		@Override
		public boolean hasNext() {
			return inputIterator.hasNext();
		}

		@Override
		public O next() {
			return transformer.call(inputIterator.next());
		}

		@Override
		public void remove() {
			inputIterator.remove();

		}
	}

	public static <T> Iterator<T> filter(Iterator<T> iterator, Filter<T> filter) {
		return new FilteredIterator<T>(filter, iterator);
	}

	public static <T> Iterator<T> concat(Iterable<Iterator<? extends T>> args) {
		Factory<Iterator<T>> factory = new Factory<Iterator<T>>() {

			@Override
			public Iterator<T> newInstance(Object... args) {
				return (Iterator<T>) args[0];
			}

		};
		return new IteratorIterator<T>(Arrays.asList(args).iterator(), factory);
	}

	public static <T> Iterator<T> concat(Iterator<? extends T>... args) {
		Factory<Iterator<T>> factory = new Factory<Iterator<T>>() {

			@Override
			public Iterator<T> newInstance(Object... args) {
				return (Iterator<T>) args[0];
			}

		};
		return new IteratorIterator<T>(Arrays.asList(args).iterator(), factory);
	}

	public static <U> Iterator<U> oneItemIterator(final U item) {
		return new Iterator<U>() {
			boolean unused = true;

			@Override
			public boolean hasNext() {
				return unused;
			}

			@Override
			public U next() {
				unused = false;
				return item;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public static Iterator emptyIterator() {
		return new Iterator() {

			@Override
			public boolean hasNext() {
				return false;
			}

			@Override
			public Object next() {
				throw new NoSuchElementException();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

		};
	}

	public static <T> Iterable<T> concat(Iterable<T> a, Iterable<T> b) {
		return able(concat(a.iterator(), b.iterator()));
	}

	public static <T> List<T> nextList(List<Iterator<T>> iterators) {
		List<T> items = new ArrayList<T>(iterators.size());
		for (Iterator<T> iter : iterators) {
			items.add(iter.next());
		}
		return items;
	}

	public static Iterator<Object> objectIterator(
			final ObjectInputStream instream) {
		return new Iterator<Object>() {
			Object next = softRead();

			@Override
			public boolean hasNext() {
				return next != null;
			}

			private Object softRead() {
				try {
					return instream.readObject();
				} catch (IOException e) {
					return null;
				} catch (ClassNotFoundException e) {
					return null;
				}
			}

			@Override
			public Object next() {
				Object curr = next;
				next = softRead();
				return curr;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

		};
	}

}
