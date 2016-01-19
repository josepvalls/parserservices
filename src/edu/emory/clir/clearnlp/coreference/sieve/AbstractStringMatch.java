package edu.emory.clir.clearnlp.coreference.sieve;

import javax.servlet.ServletContext;

import edu.emory.clir.clearnlp.coreference.mention.AbstractMention;
import edu.emory.clir.clearnlp.util.StringUtils;
/**
 * @author alexlutz
 * @version 1.0
 */
public abstract class AbstractStringMatch extends AbstractSieve {
	
	protected boolean decapitalize;
	
	public AbstractStringMatch(ServletContext context){
		super(context);
		decapitalize = false;
	}
	
	public AbstractStringMatch(ServletContext context,boolean decapitalize){
		super(context);
		setDecapitalize(decapitalize);
	}
	
	public void setDecapitalize(boolean decapitalize){
		this.decapitalize = decapitalize;
	}
	
	abstract protected String getWordSequence(AbstractMention mention);
	
	@Override
	public boolean match(AbstractMention prev, AbstractMention curr){
		String prevWords = getWordSequence(prev), currWords = getWordSequence(curr);
		if(prevWords == null || currWords == null) return false;
		
		if (decapitalize){
			prevWords = StringUtils.toLowerCase(prevWords);
			currWords = StringUtils.toLowerCase(currWords);
		}
		
		return prevWords.equals(currWords);
	}
}
