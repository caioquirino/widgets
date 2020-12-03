package com.miro.testtask.widgets.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class WidgetModel {

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Long id;

  @Column(unique = true)
  private Integer zindex;

  @Embedded
  @NonNull
  @Column
  private Coordinate coordinate;

  @NonNull
  @Column
  private int width;

  @NonNull
  @Column
  private int height;

}
