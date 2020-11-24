package com.miro.testtask.widgets.repository;

import com.miro.testtask.widgets.model.WidgetFilter;
import com.miro.testtask.widgets.model.WidgetModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.lang.ref.Cleaner;
import java.util.List;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class InMemoryWidgetRepository implements WidgetRepository, Cleaner.Cleanable {

  private final SortedMap<Integer, WidgetModel> widgetModelMap;
  private long lastId;

  public InMemoryWidgetRepository(List<WidgetModel> widgetModelList) {
    this.lastId = widgetModelList.stream().map(WidgetModel::getId).max(Long::compareTo).orElse(0L);

    this.widgetModelMap = new TreeMap<>(Integer::compareTo);

    try {
      var deduplicatedList = widgetModelList.stream()
          .collect(Collectors.toMap(WidgetModel::getId, Function.identity())).values();

      widgetModelMap.putAll(deduplicatedList.stream()
          .collect(Collectors.toMap(WidgetModel::getZindex, Function.identity())));
    } catch (IllegalStateException e) {
      throw new DuplicatedZindexException(e);
    }


  }

  public InMemoryWidgetRepository() {
    this(List.of());
  }


  @Override
  public void clean() {
    this.widgetModelMap.clear();
    this.lastId = 0;
  }

  @Override
  public List<WidgetModel> findAll() {
    return List.copyOf(this.widgetModelMap.values());
  }

  /**
   * Returns a page with the requested page.
   * Warning: This method does not support Ordering.
   *
   * @param pageable Paging config
   * @return Paged results
   */
  @Override
  public Page<WidgetModel> findPaged(Pageable pageable) {
    var pagedList = applyPaging(widgetModelMap.values().stream(), pageable)
        .collect(Collectors.toUnmodifiableList());

    return new PageImpl<>(pagedList, pageable, widgetModelMap.size());
  }

  /**
   * Returns a page with the requested page.
   * Warning: This method does not support Ordering.
   *
   * @param pageable Paging config
   * @param filter   Filter config
   * @return Filtered and paged results
   */
  @Override
  public Page<WidgetModel> findFilteredPaged(Pageable pageable, WidgetFilter filter) {
    var filteredSize = new AtomicInteger(0);

    var filteredStream = applyFiltering(widgetModelMap.values().stream(), filter)
        .peek(x -> filteredSize.incrementAndGet());

    var pagedStream = applyPaging(filteredStream, pageable);

    return new PageImpl<>(
        pagedStream.collect(Collectors.toUnmodifiableList()),
        pageable,
        filteredSize.get()
    );
  }

  public Stream<WidgetModel> applyPaging(Stream<WidgetModel> widgetStream, Pageable pageable) {
    return widgetStream
        .skip(pageable.getOffset())
        .limit(pageable.getPageSize());
  }

  public Stream<WidgetModel> applyFiltering(Stream<WidgetModel> widgetStream, WidgetFilter filter) {
    return filter == null ? widgetStream : widgetStream
        .filter(widget ->
            widget.getCoordinate().getX() >= filter.getX() &&
                widget.getCoordinate().getY() >= filter.getY() &&
                widget.getWidth() + widget.getCoordinate().getX() <= filter.getWidth() + filter.getX() &&
                widget.getHeight() + widget.getCoordinate().getY() <= filter.getHeight() + filter.getY()
        );
  }

  @Override
  public Optional<WidgetModel> findByZindex(int zindex) {
    return Optional.ofNullable((this.widgetModelMap.get(zindex)));
  }

  @Override
  public WidgetModel create(WidgetModel widgetModel) {
    checkForDuplicate(widgetModel);

    var newModel = widgetModel.toBuilder().id(++lastId).build();
    this.widgetModelMap.put(newModel.getZindex(), newModel);
    return newModel;
  }

  @Override
  public Optional<WidgetModel> findById(long id) {
    return this.widgetModelMap.values()
        .stream().filter(widgetModel -> widgetModel.getId().equals(id)).findFirst();
  }

  @Override
  public WidgetModel update(WidgetModel widgetModel) {
    checkForDuplicate(widgetModel);

    this.findById(widgetModel.getId()).ifPresent(x -> this.widgetModelMap.remove(x.getZindex()));

    this.widgetModelMap.put(widgetModel.getZindex(), widgetModel);

    return widgetModel;
  }

  private void checkForDuplicate(WidgetModel widgetModel) {
    var persisted = Optional.ofNullable(this.widgetModelMap.get(widgetModel.getZindex()));

    persisted.ifPresent(p -> {
      if (!p.getId().equals(widgetModel.getId())) {
        throw new DuplicatedZindexException(
            String.format("Duplicated z-index %d for Widget with ID %d", widgetModel.getZindex(), widgetModel.getId())
        );
      }
    });
  }

  @Override
  public int maximumZindex() {
    return this.widgetModelMap.lastKey();
  }

  @Override
  public void delete(WidgetModel widgetModel) {
    this.widgetModelMap.remove(widgetModel.getZindex());
  }
}
