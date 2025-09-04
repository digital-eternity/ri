package com.ri.converter;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.ri.model.rhythm.Rhythm;

public class RhythmSerializer extends StdSerializer<Rhythm>
{

    public RhythmSerializer() 
    {
        this(null);
    }

    public RhythmSerializer(Class<Rhythm> t) 
    {
        super(t);
    }

    @Override
    public void serialize(Rhythm rhythm, JsonGenerator jsonGenerator, 
    		SerializerProvider serializerProvider) throws IOException 
    {
          
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", rhythm.getId().toString());
        jsonGenerator.writeStringField("name", rhythm.getName());
        jsonGenerator.writeStringField("description", rhythm.getDescription());
        jsonGenerator.writeStringField("size", rhythm.getSize().toString());
        jsonGenerator.writeStringField( "baseNote", rhythm.getBaseNote().toString() );
        jsonGenerator.writeStartObject( "rhythmBlock" );
        //jsonGenerator.writeStringField("rhythm", rhythm.getRhythm().ge);
        jsonGenerator.writeEndObject();
    }
}
