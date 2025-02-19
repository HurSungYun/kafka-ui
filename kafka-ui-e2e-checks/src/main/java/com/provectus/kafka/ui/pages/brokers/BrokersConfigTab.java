package com.provectus.kafka.ui.pages.brokers;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.provectus.kafka.ui.pages.BasePage;
import io.qameta.allure.Step;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BrokersConfigTab extends BasePage {

  protected ElementsCollection editBtns = $$x("//button[@aria-label='editAction']");

  @Step
  public BrokersConfigTab waitUntilScreenReady() {
    waitUntilSpinnerDisappear();
    searchFld.shouldBe(Condition.visible);
    return this;
  }

  @Step
  public boolean isSearchByKeyVisible() {
    return isVisible(searchFld);
  }

  @Step
  public BrokersConfigTab searchConfig(String key) {
    searchItem(key);
    return this;
  }

  public List<SelenideElement> getColumnHeaders() {
    return Stream.of("Key", "Value", "Source")
        .map(name -> $x(String.format(columnHeaderLocator, name)))
        .collect(Collectors.toList());
  }

  public List<SelenideElement> getEditButtons() {
    return editBtns;
  }

  @Step
  public BrokersConfigTab clickNextButton() {
    clickNextBtn();
    waitUntilSpinnerDisappear(1);
    return this;
  }

  private List<BrokersConfigTab.BrokersConfigItem> initGridItems() {
    List<BrokersConfigTab.BrokersConfigItem> gridItemList = new ArrayList<>();
    gridItems.shouldHave(CollectionCondition.sizeGreaterThan(0))
        .forEach(item -> gridItemList.add(new BrokersConfigTab.BrokersConfigItem(item)));
    return gridItemList;
  }

  @Step
  public BrokersConfigTab.BrokersConfigItem getConfig(String key) {
    return initGridItems().stream()
        .filter(e -> e.getKey().equals(key))
        .findFirst().orElseThrow();
  }

  @Step
  public List<BrokersConfigTab.BrokersConfigItem> getAllConfigs() {
    return initGridItems();
  }

  public static class BrokersConfigItem extends BasePage {

    private final SelenideElement element;

    public BrokersConfigItem(SelenideElement element) {
      this.element = element;
    }

    @Step
    public String getKey() {
      return element.$x("./td[1]").getText().trim();
    }

    @Step
    public String getValue() {
      return element.$x("./td[2]//span").getText().trim();
    }

    @Step
    public void edit() {
      element.$x("./td[2]//button").shouldBe(Condition.enabled).click();
    }

    @Step
    public String getSource() {
      return element.$x("./td[3]").getText().trim();
    }
  }
}
