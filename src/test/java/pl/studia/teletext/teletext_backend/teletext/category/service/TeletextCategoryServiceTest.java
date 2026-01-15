package pl.studia.teletext.teletext_backend.teletext.category.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.studia.teletext.teletext_backend.common.exception.IllegalPageNumberException;
import pl.studia.teletext.teletext_backend.teletext.category.api.dto.TeletextCategoryResponse;
import pl.studia.teletext.teletext_backend.teletext.category.domain.TeletextCategory;
import pl.studia.teletext.teletext_backend.teletext.category.mapper.TeletextCategoryMapper;
import pl.studia.teletext.teletext_backend.teletext.page.api.publicapi.dto.TeletextPageResponse;
import pl.studia.teletext.teletext_backend.teletext.page.service.TeletextPageService;

@ExtendWith(MockitoExtension.class)
public class TeletextCategoryServiceTest {

  @Mock private TeletextCategoryMapper teletextCategoryMapper;

  @Mock private TeletextPageService teletextPageService;

  @InjectMocks private TeletextCategoryService teletextCategoryService;

  @Test
  void shouldReturnAllCategories() {
    // given
    when(teletextCategoryMapper.toCategoryResponse(any(TeletextCategory.class), anyInt()))
        .thenReturn(mock(TeletextCategoryResponse.class));

    // when
    var result = teletextCategoryService.getAllCategories();

    // then
    assertNotNull(result);
    assertEquals(TeletextCategory.values().length, result.length);
  }

  @Test
  void shouldReturnCategoryByName() {
    // given
    var category = TeletextCategory.NEWS;
    when(teletextPageService.getPagesByCategory(category, null)).thenReturn(List.of());
    when(teletextCategoryMapper.toCategoryResponse(any(TeletextCategory.class), anyInt()))
        .thenReturn(mock(TeletextCategoryResponse.class));

    // when
    var result = teletextCategoryService.getCategoryByName(category);

    // then
    assertNotNull(result);
  }

  @Test
  void shouldReturnCategoryForMappedSource() {
    // given
    var expected = TeletextCategory.NEWS;
    var source = expected.getMappedSources()[0];

    // when
    var result = teletextCategoryService.getCategoryByExternalDataSource(source);

    // then
    assertEquals(expected, result);
  }

  @Test
  void shouldReturnNextAvailablePageNumber() {
    // given
    var category = TeletextCategory.NEWS;
    var main = category.getMainPage();

    when(teletextPageService.getPagesByCategory(category, null))
        .thenReturn(List.of(pageResponse(main + 1), pageResponse(main + 2)));

    // when
    var result = teletextCategoryService.getNextAvailablePageNumberInCategory(category);

    // then
    assertEquals(main + 3, result);
  }

  @Test
  void shouldThrowExceptionWhenNoFreePages() {
    // given
    var category = TeletextCategory.NEWS;
    var main = category.getMainPage();

    var pages = IntStream.rangeClosed(main + 1, main + 99).mapToObj(this::pageResponse).toList();

    when(teletextPageService.getPagesByCategory(category, null)).thenReturn(pages);

    // when & then
    assertThrows(
        IllegalPageNumberException.class,
        () -> teletextCategoryService.getNextAvailablePageNumberInCategory(category));
  }

  private TeletextPageResponse pageResponse(int pageNumber) {
    return new TeletextPageResponse(null, pageNumber, null, null, null, null);
  }
}
