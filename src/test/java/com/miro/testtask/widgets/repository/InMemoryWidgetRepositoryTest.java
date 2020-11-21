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

  @BeforeEach
  public void setup() {
    repository = new InMemoryWidgetRepository(widgetFixture);
  }

  @Test
  void getAll() {
    assertEquals(widgetFixture, repository.getAll());
  }

  @Test
  void create() {
    repository = new InMemoryWidgetRepository();

    var widget =
        WidgetModel.builder()
            .coordinate(Coordinate.builder().x(4).y(5).build())
            .height(30)
            .width(300)
            .zIndex(3)
            .build();

    assertEquals(0, repository.getAll().size());

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
    assertEquals(2, repository.getAll().size());

    var widget = this.widgetFixture.get(0);

    repository.delete(widget);

    assertEquals(1, repository.getAll().size());
  }

  @Test
  void setupWithDuplicatedItems() {
    repository = new InMemoryWidgetRepository(
        List.of(
            this.widgetFixture.get(0).toBuilder().zIndex(1).build(),
            this.widgetFixture.get(0).toBuilder().zIndex(2).build()
        )
    );

    assertEquals(List.of(
        this.widgetFixture.get(0).toBuilder().zIndex(2).build()
    ), repository.getAll());
  }
}
