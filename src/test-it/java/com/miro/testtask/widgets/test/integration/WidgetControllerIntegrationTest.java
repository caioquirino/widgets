package com.miro.testtask.widgets.test.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miro.testtask.widgets.model.Coordinate;
import com.miro.testtask.widgets.model.WidgetModel;
import com.miro.testtask.widgets.repository.WidgetRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.ref.Cleaner;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    ((Cleaner.Cleanable)widgetRepository).clean();
    assertEquals(0, widgetRepository.findAll().size());
  }

  @Test
  public void testFindAllWithEmptyEntity() throws Exception {
    mockMvc.perform(get("/v1/widget/list"))
        .andExpect(content().string("[]"))
        .andExpect(status().isOk());
  }

  @Test
  public void testAddWidget() throws Exception {
    WidgetModel widget = WidgetModel.builder()
        .coordinate(Coordinate.builder().x(4).y(5).build())
        .height(30)
        .width(300)
        .zindex(3)
        .build();

    var expectation = widget.toBuilder().id(1).build();

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

    var expectedSize = widgetRepository.findAll().size() -1;

    mockMvc.perform(
        delete("/v1/widget")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(widget))
    )
        .andExpect(status().isOk());

    assertEquals(expectedSize, widgetRepository.findAll().size());
  }
}
