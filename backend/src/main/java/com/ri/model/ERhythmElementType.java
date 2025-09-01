package com.ri.model;


public enum ERhythmElementType
{
	PAUSE( true ),
	NOTE( false );
	
	private Boolean isPause;
	
	private ERhythmElementType( Boolean v )
	{
		isPause = v;
	}
	
	public Boolean getValue()
	{
		return isPause;
	}
	
}
