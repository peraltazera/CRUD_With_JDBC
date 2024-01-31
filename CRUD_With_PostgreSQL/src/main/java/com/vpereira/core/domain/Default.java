package com.vpereira.core.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vpereira.annotation.Column;
import com.vpereira.annotation.Id;
import com.vpereira.annotation.Table;

import java.util.Objects;

@Table("Default_Table")
public class Default implements Entity{

    @Id
    private Long id;
    @Column(value = "var_integer", notNull = true)
    private Integer varInteger;
    @JsonProperty("varFloat")
    @Column(value = "var_float", notNull = true, get = "varFloatGet")
    private Float varFloat;
    @Column(notNull = true, set = "varDoubleSet")
    private Double varDouble;
    @Column(value = "var_character", set = "varCharacterSet")
    private Character varCharacter;
    @JsonProperty("varBoolean")
    @Column(value = "var_boolean", get = "varBooleanGet")
    private Boolean varBoolean;
    @Column
    private String varString;
    @Column(value = "var_string_length", length = 50)
    private String varStringLength;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVarInteger() {
        return varInteger;
    }

    public void setVarInteger(Integer varInteger) {
        this.varInteger = varInteger;
    }

    @JsonProperty("varFloat")
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

    @JsonProperty("varBoolean")
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
