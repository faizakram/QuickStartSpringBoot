package com.app.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Document(indexName = "patrons")
@Data
public class PatronElastic {
    @Id
    private Integer id;
    @Field(type = FieldType.Text)
    private String first;
    @Field(type = FieldType.Text)
    private String last;
    @Field(type = FieldType.Keyword)
    private String dob;
    @Field(type = FieldType.Text)
    private String created;
    @Field(type = FieldType.Text)
    private String modified;
    @Field(type = FieldType.Text)
    private String email;
    @Field(type = FieldType.Keyword)
    private String playerId;
    @Field(type = FieldType.Keyword)
    private String gender;
    @Field(type = FieldType.Integer)
    private Integer parentId;
}