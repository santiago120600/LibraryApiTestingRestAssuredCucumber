package io.github.santiago120600.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public abstract class BaseModel{
    String message;    
}
