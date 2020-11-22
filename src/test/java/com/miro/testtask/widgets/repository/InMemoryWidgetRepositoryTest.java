package com.miro.testtask.widgets.repository;

import com.miro.testtask.widgets.model.Coordinate;
import com.miro.testtask.widgets.model.WidgetModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryWidgetRepositoryTest {

  private InMemoryWidgetRepository repository;

  private final List<WidgetModel> widgetFixture = List.of(
      WidgetModel.builder()
          .coordinate(Coordinate.builder().x(1).y(2).build())
          .height(10)
          .width(100)
          .zindex(1)
          .id(1L)
          .build(),
      WidgetModel.builder()
          .coordinate(Coordinate.builder().x(3).y(4).build())
          .height(10)
          .width(100)
          .zindex(5)
          .id(2L)
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
        widget.toBuilder().id(1L).build(),
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
  void updateToFailIfZindexIsDuplicated() {
    var widget = this.widgetFixture.get(0).toBuilder().zindex(this.widgetFixture.get(1).getZindex()).build();

    assertThrows(DuplicatedZindexException.class, () -> repository.update(widget));
  }

  @Test
  void delete() {
    assertEquals(2, repository.findAll().size());

    var widget = this.widgetFixture.get(0);

    repository.delete(widget);

    assertEquals(1, repository.findAll().size());
  }

  @Test
  void setupWithDuplicatedZindex() {
    assertThrows(DuplicatedZindexException.class, () ->
        new InMemoryWidgetRepository(
            List.of(
                this.widgetFixture.get(0).toBuilder().zindex(1).build(),
                this.widgetFixture.get(0).toBuilder().zindex(2).build()
            )
        )
    );
  }

  @Test
  void setupWithDuplicatedId() {
    assertThrows(DuplicatedZindexException.class, () ->
        new InMemoryWidgetRepository(
            List.of(
                this.widgetFixture.get(1).toBuilder().id(2L).build(),
                this.widgetFixture.get(1).toBuilder().id(2L).build()
            )
        )
    );
  }

  @Test
  void maximumZindex() {
    assertEquals(5, repository.maximumZindex());
  }

  @Test
  void findByZindex() {
    assertEquals(this.widgetFixture.get(0), repository.findByZindex(1).orElseThrow());
    assertEquals(this.widgetFixture.get(1), repository.findByZindex(5).orElseThrow());
  }

  @Test
  void clean() {
    assertEquals(2, repository.findAll().size());
    repository.clean();
    assertEquals(0, repository.findAll().size());
  }
}
