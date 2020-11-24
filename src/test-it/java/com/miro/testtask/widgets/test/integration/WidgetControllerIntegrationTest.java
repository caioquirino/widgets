package com.miro.testtask.widgets.test.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miro.testtask.widgets.model.Coordinate;
import com.miro.testtask.widgets.model.WidgetModel;
import com.miro.testtask.widgets.repository.WidgetRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.ref.Cleaner;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WidgetControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private WidgetRepository widgetRepository;

  @AfterEach
  public void cleanup() {
    ((Cleaner.Cleanable) widgetRepository).clean();
    assertEquals(0, widgetRepository.findAll().size());
  }


  @Test
  public void testFindAllWithEmptyEntity() throws Exception {
    var createdWidget = objectMapper.readValue(
        mockMvc.perform(get("/v1/widget/list"))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString(),
        new TypeReference<List<WidgetModel>>() {
        }
    );

    assertEquals(0, createdWidget.size());
  }

  @Test
  public void testFindAllWithPaging() throws Exception {
    var widgetList = IntStream.range(1, 20).mapToObj(i ->
        WidgetModel.builder()
            .coordinate(Coordinate.builder().x(4).y(5).build())
            .height(30)
            .width(300)
            .zindex(i)
            .build()
    )
        .map(widgetRepository::create)
        .collect(Collectors.toUnmodifiableList());

    var createdWidget = objectMapper.readValue(
        mockMvc.perform(get("/v1/widget/list?page=1&pageSize=10"))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString(),
        new TypeReference<List<WidgetModel>>() {
        }
    );

    assertEquals(
        widgetList.
            stream()
            .skip(10)
            .limit(10)
            .collect(Collectors.toUnmodifiableList()),
        createdWidget
    );
  }

  @Test
  public void testFindAllWithFilter() throws Exception {
    var widgetList = List.of(
        WidgetModel.builder()
            .coordinate(Coordinate.builder().x(0).y(0).build())
            .height(100)
            .width(100)
            .zindex(1)
            .build(),
        WidgetModel.builder()
            .coordinate(Coordinate.builder().x(0).y(50).build())
            .height(100)
            .width(100)
            .zindex(2)
            .build(),
        WidgetModel.builder()
            .coordinate(Coordinate.builder().x(50).y(50).build())
            .height(100)
            .width(100)
            .zindex(3)
            .build()
    ).stream()
        .map(widgetRepository::create)
        .collect(Collectors.toUnmodifiableList());

    var createdWidget = objectMapper.readValue(
        mockMvc.perform(get("/v1/widget/list?filterX=0&filterY=0&filterWidth=100&filterHeight=150"))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString(),
        new TypeReference<List<WidgetModel>>() {
        }
    );

    assertEquals(
        widgetList.
            stream()
            .limit(2)
            .collect(Collectors.toUnmodifiableList()),
        createdWidget
    );
  }

  @Test
  public void testUpdateWidget() throws Exception {
    WidgetModel widget = this.widgetRepository.create(WidgetModel.builder()
        .coordinate(Coordinate.builder().x(4).y(5).build())
        .height(30)
        .width(300)
        .zindex(3)
        .build());

    var newWidgetVersion = widget.toBuilder().height(500).build();

    var currentSize = widgetRepository.findAll().size();

    mockMvc.perform(
        patch("/v1/widget")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(newWidgetVersion))
    )
        .andExpect(content().string(objectMapper.writeValueAsString(newWidgetVersion)))
        .andExpect(status().isOk());

    assertEquals(currentSize, widgetRepository.findAll().size());
    assertEquals(newWidgetVersion, widgetRepository.findAll().get(0));
  }

  @Test
  public void testDeleteWidget() throws Exception {
    WidgetModel widget = this.widgetRepository.create(WidgetModel.builder()
        .coordinate(Coordinate.builder().x(4).y(5).build())
        .height(30)
        .width(300)
        .zindex(3)
        .build());

    var expectedSize = widgetRepository.findAll().size() - 1;

    mockMvc.perform(
        delete("/v1/widget")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(widget))
    )
        .andExpect(status().isOk());

    assertEquals(expectedSize, widgetRepository.findAll().size());
  }

  @Test
  public void testAddWidget() throws Exception {
    WidgetModel widget = WidgetModel.builder()
        .coordinate(Coordinate.builder().x(4).y(5).build())
        .height(30)
        .width(300)
        .zindex(3)
        .build();

    var expectation = widget.toBuilder().id(1L).build();

    var currentSize = widgetRepository.findAll().size();

    mockMvc.perform(
        post("/v1/widget")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(widget))
    )
        .andExpect(content().string(objectMapper.writeValueAsString(expectation)))
        .andExpect(status().isOk());

    assertEquals(currentSize + 1, widgetRepository.findAll().size());
    assertEquals(expectation, widgetRepository.findAll().get(0));
  }


  private static List<Expectation> buildExpectations(List<Integer> zindexList, List<Long> idList) {
    return IntStream.range(0, zindexList.size())
        .mapToObj(x -> new Expectation(idList.get(x), zindexList.get(x)))
        .collect(Collectors.toUnmodifiableList());
  }

  private static Stream<Arguments> zindexSlideScenario() {
    return List.of(
        new WidgetServiceScenario(List.of(1, 2, 3), 2, buildExpectations(List.of(1, 2, 3, 4), List.of(1L, 4L, 2L, 3L))),
        new WidgetServiceScenario(List.of(1, 5, 6), 2, buildExpectations(List.of(1, 2, 5, 6), List.of(1L, 4L, 2L, 3L))),
        new WidgetServiceScenario(List.of(1, 2, 4), 2, buildExpectations(List.of(1, 2, 3, 4), List.of(1L, 4L, 2L, 3L)))
    ).stream().map(Arguments::of);
  }

  @ParameterizedTest
  @MethodSource("zindexSlideScenario")
  void testZindexSlide(WidgetServiceScenario scenario) throws Exception {
    scenario.given.forEach(x -> this.widgetRepository.create(WidgetModel.builder()
        .coordinate(Coordinate.builder().x(1).y(2).build())
        .height(10)
        .width(100)
        .zindex(x)
        .build())
    );

    var newWidget = WidgetModel.builder()
        .coordinate(Coordinate.builder().x(1).y(2).build())
        .height(10)
        .width(100)
        .zindex(scenario.getNewItem()).build();

    var createdWidget = objectMapper.readValue(mockMvc.perform(
        post("/v1/widget")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(newWidget))
    )
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString(), WidgetModel.class);

    assertEquals(scenario.newItem, createdWidget.getZindex());

    var result = this.widgetRepository.findAll()
        .stream().map(x -> new Expectation(x.getId(), x.getZindex()))
        .collect(Collectors.toUnmodifiableList());

    assertEquals(scenario.result, result);

  }

  @Data
  @AllArgsConstructor
  private static class WidgetServiceScenario {
    private List<Integer> given;
    private int newItem;
    private List<Expectation> result;
  }

  @Data
  @AllArgsConstructor
  private static class Expectation {
    private Long id;
    private int zindex;
  }

}
