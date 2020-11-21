package com.miro.testtask.widgets.controller;

import com.miro.testtask.widgets.model.WidgetModel;
import com.miro.testtask.widgets.service.WidgetService;

import java.util.List;

public class WidgetController {

  private final WidgetService widgetService;

  public WidgetController(
    WidgetService widgetService
  ) {
    this.widgetService = widgetService;
  }

  public WidgetModel create(WidgetModel widget) {
    return widgetService.create(widget);
  }

  public List<WidgetModel> getAll() {
    return widgetService.getAll();
  }

  public WidgetModel update(WidgetModel widget) {
    return widgetService.update(widget);
  }

  public void delete(WidgetModel widget) {
    widgetService.delete(widget);
  }

}
