package com.miro.testtask.widgets.repository;

import com.miro.testtask.widgets.model.Coordinate;
import com.miro.testtask.widgets.model.WidgetModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryWidgetRepositoryTest {

  private InMemoryWidgetRepository repository;

  private final List<WidgetModel> widgetFixture = List.of(
      WidgetModel.builder()
          .coordinate(Coordinate.builder().x(1).y(2).build())
          .height(10)
          .width(100)
          .zindex(1)
          .id(1)
          .build(),
      WidgetModel.builder()
          .coordinate(Coordinate.builder().x(3).y(4).build())
          .height(10)
          .width(100)
          .zindex(5)
          .id(2)
          .build()
  );

  @BeforeEach
  public void setup() {
    repository = new InMemoryWidgetRepository(widgetFixture);
  }

  @Test
  void findAll() {
    assertEquals(widgetFixture, repository.findAll());
  }

  @Test
  void create() {
    repository = new InMemoryWidgetRepository();

    var widget =
        WidgetModel.builder()
            .coordinate(Coordinate.builder().x(4).y(5).build())
            .height(30)
            .width(300)
            .zindex(3)
            .build();

    assertEquals(0, repository.findAll().size());

    var result = repository.create(widget);
    assertEquals(
        widget.toBuilder().id(1).build(),
        result
    );
  }

  @Test
  void update() {
    var widget = this.widgetFixture.get(0);

    var result = repository.update(widget);
    assertEquals(
        widget,
        result
    );
  }

  @Test
  void delete() {
    assertEquals(2, repository.findAll().size());

    var widget = this.widgetFixture.get(0);

    repository.delete(widget);

    assertEquals(1, repository.findAll().size());
  }

  @Test
  void setupWithDuplicatedItems() {
    repository = new InMemoryWidgetRepository(
        List.of(
            this.widgetFixture.get(0).toBuilder().zindex(1).build(),
            this.widgetFixture.get(0).toBuilder().zindex(2).build()
        )
    );

    assertEquals(List.of(
        this.widgetFixture.get(0).toBuilder().zindex(2).build()
    ), repository.findAll());
  }

  @Test
  void maximumzindex() {
    assertEquals(5, repository.maximumzindex());
  }
}
