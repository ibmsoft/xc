package com.cubedrive.base.utils;

import java.io.IOException;
import java.util.Objects;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.cubedrive.base.utils.Pair.PairSerializer;

@JsonSerialize(using=PairSerializer.class)
public class Pair<L, R> {

    private final L left;
    private final R right;

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) return true;
        if (!(o instanceof Pair))
            return false;
        Pair<?,?> other = (Pair<?,?>) o;
        return Objects.equals(left, other.left)  && Objects.equals(right,other.right);
    }
    
    public static class PairSerializer extends JsonSerializer<Pair<?,?>> {

        @Override
        public void serialize(Pair<?,?> pair, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            jgen.writeStartArray();
            jgen.writeObject(pair.left);
            jgen.writeObject(pair.right);
            jgen.writeEndArray();
        }
    }

}
