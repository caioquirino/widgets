package com.miro.testtask.widgets.repository;

import com.miro.testtask.widgets.model.WidgetModel;

import java.util.List;
import java.util.Optional;

public interface WidgetRepository {
  List<WidgetModel> findAll();

  Optional<WidgetModel> findByZindex(int zindex);

  WidgetModel create(WidgetModel widgetModel);

  WidgetModel update(WidgetModel widgetModel);


  int maximumZindex();

  void delete(WidgetModel widgetModel);
}
