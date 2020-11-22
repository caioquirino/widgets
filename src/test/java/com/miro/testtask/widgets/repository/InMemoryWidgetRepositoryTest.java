package com.miro.testtask.widgets.repository;

import com.miro.testtask.widgets.model.Coordinate;
import com.miro.testtask.widgets.model.WidgetModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

  @Test
  void findPaged() {
    repository = new InMemoryWidgetRepository();

    var createdItems = IntStream.range(1, 26).mapToObj(i -> repository.create(
        WidgetModel.builder()
            .coordinate(Coordinate.builder().x(1).y(2).build())
            .height(10)
            .width(100)
            .zindex(i)
            .build()
    )).collect(Collectors.toList());

    Pageable pageRequest = PageRequest.of(0, 10);

    var page1 = repository.findPaged(pageRequest);

    assertEquals(0, page1.getNumber());
    assertEquals(10, page1.getNumberOfElements());
    assertEquals(25, page1.getTotalElements());
    assertEquals(3, page1.getTotalPages());
    assertEquals(createdItems.stream().limit(10).collect(Collectors.toList()), page1.getContent());

    pageRequest = pageRequest.next();
    var page2 = repository.findPaged(pageRequest);

    assertEquals(1, page2.getNumber());
    assertEquals(10, page2.getNumberOfElements());
    assertEquals(25, page2.getTotalElements());
    assertEquals(3, page2.getTotalPages());
    assertEquals(createdItems.stream().skip(10).limit(10).collect(Collectors.toList()), page2.getContent());

    pageRequest = pageRequest.next();
    var page3 = repository.findPaged(pageRequest);

    assertEquals(2, page3.getNumber());
    assertEquals(5, page3.getNumberOfElements());
    assertEquals(25, page3.getTotalElements());
    assertEquals(3, page3.getTotalPages());
    assertEquals(createdItems.stream().skip(20).limit(5).collect(Collectors.toList()), page3.getContent());

    pageRequest = pageRequest.next();
    var page4 = repository.findPaged(pageRequest);

    assertEquals(3, page4.getNumber());
    assertEquals(0, page4.getNumberOfElements());
    assertEquals(25, page4.getTotalElements());
    assertEquals(3, page4.getTotalPages());
    assertEquals(List.of(), page4.getContent());
  }
}
