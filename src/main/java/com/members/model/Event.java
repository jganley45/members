package com.members.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.eclipse.persistence.annotations.ConversionValue;
import org.eclipse.persistence.annotations.ObjectTypeConverter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "event")
@ObjectTypeConverter(name = "booleanConverter", dataType = Integer.class, objectType = Boolean.class,
    conversionValues = {
        @ConversionValue(dataValue = "1", objectValue = "true"),
        @ConversionValue(dataValue = "0", objectValue = "false")})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class Event {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "location")
  private String location;

  @Column(name = "description")
  private String description;

  @Column(name = "name")
  private String eventName;

  @Column(name = "date")
  private Date eventDate;


}
