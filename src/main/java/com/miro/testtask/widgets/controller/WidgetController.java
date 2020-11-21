package com.miro.testtask.widgets.controller;

import com.miro.testtask.widgets.model.WidgetModel;
import com.miro.testtask.widgets.service.WidgetService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WidgetController {

  private final WidgetService widgetService;

  public WidgetController(
      WidgetService widgetService
  ) {
    this.widgetService = widgetService;
  }

  @RequestMapping(path = "/v1/widget", method = RequestMethod.POST)
  public WidgetModel create(@RequestBody WidgetModel widget) {
    return widgetService.create(widget);
  }

  @RequestMapping(path = "/v1/widget/list")
  public List<WidgetModel> findAll() {
    return widgetService.findAll();
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
