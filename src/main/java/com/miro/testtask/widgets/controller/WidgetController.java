package com.miro.testtask.widgets.controller;

import com.miro.testtask.widgets.model.WidgetModel;
import com.miro.testtask.widgets.service.WidgetService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WidgetController {

  private final WidgetService widgetService;

  private final int defaultPageSize;

  public WidgetController(
      WidgetService widgetService,

      @Value("${widgets.page_size.default}")
          int defaultPageSize
  ) {
    this.defaultPageSize = defaultPageSize;
    this.widgetService = widgetService;
  }

  @RequestMapping(path = "/v1/widget", method = RequestMethod.POST)
  public WidgetModel create(@RequestBody WidgetModel widget) {
    return widgetService.create(widget);
  }

  @RequestMapping(path = "/v1/widget/list")
  public List<WidgetModel> findAll(
      @RequestParam @Nullable Integer page,
      @RequestParam @Nullable Integer size) {

    return widgetService.findPaged(
        PageRequest.of(
            page == null ? 0 : page,
            size == null ? defaultPageSize : size
        )
    ).getContent();
  }

  @RequestMapping(path = "/v1/widget", method = RequestMethod.PATCH)
  public WidgetModel update(@RequestBody WidgetModel widget) {
    return widgetService.update(widget);
  }

  @RequestMapping(path = "/v1/widget", method = RequestMethod.DELETE)
  public void delete(@RequestBody WidgetModel widget) {
    widgetService.delete(widget);
  }

}
