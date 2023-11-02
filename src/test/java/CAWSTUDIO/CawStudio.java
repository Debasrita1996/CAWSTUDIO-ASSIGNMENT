package CAWSTUDIO;



import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CawStudio{
	public static void main(String[] args) {
		
		// Initialize the WebDriver
		WebDriver driver = new ChromeDriver();

		// Navigate to the given URL
		driver.get("https://testpages.herokuapp.com/styled/tag/dynamic-table.html");
		driver.manage().window().maximize();
		WebDriverWait wait =new WebDriverWait(driver, Duration.ofSeconds(30));

		
		try {
			// Find and click the "Table Data" button
			
			WebElement tableDataButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='tablehere']/following-sibling::details/summary")));
			tableDataButton.click();

			
			// Input String Data as JSONArray
			JsonArray jsonArray = JsonParser.parseString("[{\"name\" : \"Bob\", \"age\" : 20, \"gender\": \"male\"}, "
					+ "{\"name\": \"George\", \"age\" : 42, \"gender\": \"male\"}, "
					+ "{\"name\": \"Sara\", \"age\" : 42, \"gender\": \"female\"}, "
					+ "{\"name\": \"Conor\", \"age\" : 40, \"gender\": \"male\"}, "
					+ "{\"name\": \"Jennifer\", \"age\" : 42, \"gender\": \"female\"}]").getAsJsonArray();

			// *********************** Check for data duplication ***************************************
			
			// Create a set to store unique entries based on the compound key
			Set<String> uniqueEntries = new HashSet();

			// Create a new JSON array for unique entries
			JsonArray uniqueJsonArray = new JsonArray();

			for (JsonElement element : jsonArray) {
				JsonObject jsonObject = element.getAsJsonObject();
				String compoundKey = jsonObject.get("name").getAsString() + jsonObject.get("age").getAsString()
						+ jsonObject.get("gender").getAsString();

				// Check if the compound key is a duplicate entry
				if (!uniqueEntries.contains(compoundKey)) {
					// Add the unique entry to the set and new JSON array
					uniqueEntries.add(compoundKey);
					uniqueJsonArray.add(jsonObject);
				}
			}
			
			// *******************************************************************************************

			// Data input in input text box:
			WebElement inputTextBox = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//textarea[@id='jsondata']")));
			inputTextBox.clear();
			inputTextBox.sendKeys(uniqueJsonArray.toString());
			

			// Click the "Refresh Table" button
			WebElement refreshTableButton = driver.findElement(By.xpath("//button[@id='refreshtable']"));
			refreshTableButton.click();

			// Wait for the table to populate
			Thread.sleep(5000);
			
			
			// Retrieve the rows from the UI table
			List<WebElement> tableRows = driver.findElements(By.xpath("//table[@id='dynamictable']/tr"));
			

			// validation
			for (int i = 1; i <= (tableRows.size()-1) ; i++) {

				for (int j = 1; j <= 3; j++) {

					String tabledata = driver
							.findElement(By.xpath("//table[@id='dynamictable']/tr[" + (i + 1) + "]/td[" + j + "]"))
							.getText();

					JsonObject jsonObject = jsonArray.get(i - 1).getAsJsonObject();

					switch (j) {

					case 1:
						if (tabledata.equals(jsonObject.get("name").getAsString())) {
							System.out.println("UI Data: " + tabledata + " matches with expected data: "
									+ jsonObject.get("name").getAsString());
						} else {
							System.out.println("UI Data: " + tabledata + " does not matches with expected data: "
									+ jsonObject.get("name").getAsString());
						}

						break;
					case 2:
						if (tabledata.equals(jsonObject.get("age").getAsString())) {
							System.out.println("UI Data: " + tabledata + " matches with expected data: "
									+ jsonObject.get("age").getAsString());
						} else {
							System.out.println("UI Data: " + tabledata + " does not matches with expected data: "
									+ jsonObject.get("age").getAsString());
						}

						break;

					case 3:
						if (tabledata.equals(jsonObject.get("gender").getAsString())) {
							System.out.println("UI Data: " + tabledata + " matches with expected data: "
									+ jsonObject.get("gender").getAsString());
						} else {
							System.out.println("UI Data: " + tabledata + " does not matches with expected data: "
									+ jsonObject.get("gender").getAsString());
						}

						break;
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Close the WebDriver
			driver.quit();
		}
	}

}
