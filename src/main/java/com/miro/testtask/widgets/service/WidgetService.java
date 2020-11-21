package com.miro.testtask.widgets.service;


import com.miro.testtask.widgets.model.WidgetModel;
import com.miro.testtask.widgets.repository.WidgetRepository;

import java.util.List;

public class WidgetService {

  private final WidgetRepository repository;

  public WidgetService(WidgetRepository repository) {
    this.repository = repository;
  }

  public List<WidgetModel> getAll() {
    return repository.getAll();
  }

  public WidgetModel create(WidgetModel widgetModel) {
    return repository.create(widgetModel);
  }
  public WidgetModel update(WidgetModel widgetModel) {
    return repository.update(widgetModel);
  }
  public void delete(WidgetModel widgetModel) {
    repository.delete(widgetModel);
  }

}
