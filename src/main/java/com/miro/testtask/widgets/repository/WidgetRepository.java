package com.miro.testtask.widgets.repository;

import com.miro.testtask.widgets.model.WidgetModel;

import java.util.List;

public interface WidgetRepository {
  List<WidgetModel> findAll();

  WidgetModel create(WidgetModel widgetModel);

  WidgetModel update(WidgetModel widgetModel);

  int maximumzindex();

  void delete(WidgetModel widgetModel);
}
