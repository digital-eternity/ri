package com.ri.model.preset;

import java.util.List;

public class ClassicPreset extends RIPreset
{
	public ClassicPreset()
	{
		super.name = "Classic";
		super.elements = List.of("click");
	}
}
