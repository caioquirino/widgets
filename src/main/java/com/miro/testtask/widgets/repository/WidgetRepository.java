package com.miro.testtask.widgets.repository;

import com.miro.testtask.widgets.model.WidgetModel;

import java.util.List;

public interface WidgetRepository {
  List<WidgetModel> getAll();
  WidgetModel create(WidgetModel widgetModel);
  WidgetModel update(WidgetModel widgetModel);
  void delete(WidgetModel widgetModel);
}
