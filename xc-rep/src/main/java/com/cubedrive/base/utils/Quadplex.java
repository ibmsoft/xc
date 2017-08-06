package com.cubedrive.base.utils;

import java.io.IOException;
import java.util.Objects;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.cubedrive.base.utils.Quadplex.QuadplexSerializer;

@JsonSerialize(using=QuadplexSerializer.class)
public class Quadplex<T1,T2,T3, T4>{
    private final T1 t1;
    private final T2 t2;
    private final T3 t3;
    private final T4 t4;
    
    public Quadplex(T1 t1, T2 t2, T3 t3, T4 t4) {
        super();
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
        this.t4 = t4;
    }

    public T1 getT1() {
        return t1;
    }

    public T2 getT2() {
        return t2;
    }

    public T3 getT3() {
        return t3;
    }
    
    public T4 getT4(){
        return t4;
    }
    
    public int hashCode(){
        return Objects.hash(t1,t2,t3, t4);
    }
    
    public boolean equals(Object o){
        if(o == this) return true;
        if(!(o instanceof Quadplex))return false;
        Quadplex<?,?,?,?> other = (Quadplex<?,?,?,?>)o;
        return Objects.equals(t1, other.t1) && Objects.equals(t2, other.t2) && Objects.equals(t3, other.t3) && Objects.equals(t4, other.t4);
    }
    
    
    public static class QuadplexSerializer extends JsonSerializer<Quadplex<?,?,?,?>> {

        @Override
        public void serialize(Quadplex<?,?,?, ?> quadplex, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            jgen.writeStartArray();
            jgen.writeObject(quadplex.t1);
            jgen.writeObject(quadplex.t2);
            jgen.writeObject(quadplex.t3);
            jgen.writeObject(quadplex.t4);
            jgen.writeEndArray();
        }
    }
    
    public static void main(String []args){
        Quadplex<Integer,Integer,Integer, Integer> q1 = new Quadplex(1,2,3,4);
        Quadplex<Integer,Integer,Integer, Integer> q2 = new Quadplex(1,2,3,4);
        System.out.println(JsonUtil.toJson(q1));
    }

}
