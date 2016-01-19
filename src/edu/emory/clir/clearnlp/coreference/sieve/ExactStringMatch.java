package edu.emory.clir.clearnlp.coreference.sieve;

import javax.servlet.ServletContext;

import edu.emory.clir.clearnlp.coreference.mention.AbstractMention;

/**
 * @author alexlutz
 */
public class ExactStringMatch extends AbstractStringMatch {
	public ExactStringMatch(ServletContext context){
		super(context);
	}
	
	public ExactStringMatch(ServletContext context, boolean decapitalize){
		super(context,decapitalize);
	}
	
	@Override
	protected String getWordSequence(AbstractMention mention){
		return mention.getSubTreeWordSequence();
	}
}
