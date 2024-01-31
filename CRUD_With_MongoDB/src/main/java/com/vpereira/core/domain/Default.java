package com.vpereira.core.domain;

import com.vpereira.annotation.FieldMongoDB;
import com.vpereira.annotation.Id;
import com.vpereira.annotation.DocumentMongoDB;

import java.util.Objects;

@DocumentMongoDB("Default_Table")
public class Default implements Entity{

    @Id
    private String id;
    @FieldMongoDB(value = "var_integer")
    private Integer varInteger;
    @FieldMongoDB(value = "var_float", get = "varFloatGet")
    private Float varFloat;
    @FieldMongoDB(set = "varDoubleSet")
    private Double varDouble;
    @FieldMongoDB(value = "var_character", set = "varCharacterSet")
    private Character varCharacter;
    @FieldMongoDB(value = "var_boolean", get = "varBooleanGet")
    private Boolean varBoolean;
    @FieldMongoDB
    private String varString;
    @FieldMongoDB(value = "var_string_length")
    private String varStringLength;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public Integer getVarInteger() {
        return varInteger;
    }

    public void setVarInteger(Integer varInteger) {
        this.varInteger = varInteger;
    }

    public Float varFloatGet() {
        return varFloat;
    }

    public void setVarFloat(Float varFloat) {
        this.varFloat = varFloat;
    }

    public Double getVarDouble() {
        return varDouble;
    }

    public void varDoubleSet(Double varDouble) {
        this.varDouble = varDouble;
    }

    public Character getVarCharacter() {
        return varCharacter;
    }

    public void varCharacterSet(Character varCharacter) {
        this.varCharacter = varCharacter;
    }

    public Boolean varBooleanGet() {
        return varBoolean;
    }

    public void setVarBoolean(Boolean varBoolean) {
        this.varBoolean = varBoolean;
    }

    public String getVarString() {
        return varString;
    }

    public void setVarString(String varString) {
        this.varString = varString;
    }

    public String getVarStringLength() {
        return varStringLength;
    }

    public void setVarStringLength(String varStringLength) {
        this.varStringLength = varStringLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Default aDefault = (Default) o;
        return Objects.equals(id, aDefault.id) && Objects.equals(varInteger, aDefault.varInteger) && Objects.equals(varFloat, aDefault.varFloat) && Objects.equals(varDouble, aDefault.varDouble) && Objects.equals(varCharacter, aDefault.varCharacter) && Objects.equals(varBoolean, aDefault.varBoolean) && Objects.equals(varString, aDefault.varString) && Objects.equals(varStringLength, aDefault.varStringLength);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, varInteger, varFloat, varDouble, varCharacter, varBoolean, varString, varStringLength);
    }

    @Override
    public String toString() {
        return "Default{" +
                "id=" + id +
                ", varInteger=" + varInteger +
                ", varFloat=" + varFloat +
                ", varDouble=" + varDouble +
                ", varCharacter=" + varCharacter +
                ", varBoolean=" + varBoolean +
                ", varString='" + varString + '\'' +
                ", varStringLength='" + varStringLength + '\'' +
                '}';
    }
}
