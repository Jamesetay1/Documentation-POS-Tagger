from selenium import webdriver
from selenium.webdriver.support.expected_conditions import staleness_of
from selenium.webdriver.support.ui import WebDriverWait
import time

class Loader:
    def __init__(self):
        self.driver = webdriver.Chrome()

    def load(self, url):
        self.driver.get(url)
        self.wait_for_page_load()
        time.sleep(1) # Wait an extra second to ensure that the page actually loaded
        return self.driver.page_source

    def wait_for_page_load(self, timeout=10):
        old_page = self.driver.find_element_by_tag_name('html')
        yield
        WebDriverWait(self, timeout).until(staleness_of(old_page))