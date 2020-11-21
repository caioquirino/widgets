package com.miro.testtask.widgets.repository;

import com.miro.testtask.widgets.model.Coordinate;
import com.miro.testtask.widgets.model.WidgetModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryWidgetRepositoryTest {

  private InMemoryWidgetRepository repository;

  @BeforeEach
  public void setup() {
    repository = new InMemoryWidgetRepository();
  }

  @Test
  void getAll() {
    var widgetList = List.of(
        WidgetModel.builder()
            .coordinate(Coordinate.builder().x(1).y(2).build())
            .height(10)
            .width(100)
            .zIndex(1)
            .id(1)
            .build(),
        WidgetModel.builder()
            .coordinate(Coordinate.builder().x(3).y(4).build())
            .height(10)
            .width(100)
            .zIndex(2)
            .id(2)
            .build()
    );

    assertEquals(widgetList, repository.getAll());
  }

  @Test
  void create() {
    var widget = WidgetModel.builder()
        .coordinate(Coordinate.builder().x(3).y(4).build())
        .height(10)
        .width(100)
        .zIndex(2)
        .build();

    var result = repository.create(widget);
    assertEquals(
        widget.toBuilder().id(1).build(),
        result
    );
  }

  @Test
  void update() {
    var widget = WidgetModel.builder()
        .coordinate(Coordinate.builder().x(3).y(4).build())
        .height(10)
        .width(100)
        .zIndex(2)
        .id(1)
        .build();

    var result = repository.update(widget);
    assertEquals(
        widget,
        result
    );
  }

  @Test
  void delete() {
    var widget = WidgetModel.builder()
        .coordinate(Coordinate.builder().x(3).y(4).build())
        .height(10)
        .width(100)
        .zIndex(2)
        .id(1)
        .build();

    repository.delete(widget);
  }
}
