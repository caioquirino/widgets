package com.miro.testtask.widgets.controller;

import com.miro.testtask.widgets.model.WidgetFilter;
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
      @RequestParam @Nullable Integer pageSize,
      @RequestParam @Nullable Integer filterX,
      @RequestParam @Nullable Integer filterY,
      @RequestParam @Nullable Integer filterWidth,
      @RequestParam @Nullable Integer filterHeight
  ) {
    var pageRequest = PageRequest.of(
        page == null ? 0 : page,
        pageSize == null ? defaultPageSize : pageSize
    );
    var filter = filterX != null &&
        filterY != null &&
        filterWidth != null &&
        filterHeight != null ?
        WidgetFilter.
            builder()
            .x(filterX)
            .y(filterY)
            .width(filterWidth)
            .height(filterHeight)
            .build() : null;

    return widgetService.findFilteredPaged(pageRequest, filter).getContent();
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
