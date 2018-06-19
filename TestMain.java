package tatoc;

import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testng.*;
import org.testng.asserts.*;
import org.testng.annotations.*;
import org.testng.annotations.Test;

public class TestMain 
{
	WebDriver driver;

	 public TestMain()
	{
		System.setProperty("webdriver.chrome.driver","/home/princegupta/Downloads/chromedriver");
		driver = new ChromeDriver();

		
	}
	
	
		@Test
		public void basiccourse() 
		{
		driver.get("http://10.0.1.86/tatoc");
		driver.findElement(By.linkText("Basic Course")).click();
		Assert.assertEquals("http://10.0.1.86/tatoc/basic/grid/gate", driver.getCurrentUrl());
		}
		
		@Test(dependsOnMethods= {"basiccourse"})
		public void greenbox() 
		{
			Assert.assertEquals(driver.findElement(By.className("greenbox")).isDisplayed(), true);
		WebElement findGreen = driver.findElement(By.className("greenbox"));
		findGreen.click();
		Assert.assertEquals("http://10.0.1.86/tatoc/basic/frame/dungeon",driver.getCurrentUrl());
		
		}
		
		@Test(dependsOnMethods= {"greenbox"})
		public void frame() 
		{
		driver.switchTo().frame("main");
		Assert.assertEquals(driver.findElement(By.id("answer")).isDisplayed(), true);

		String color1 = driver.findElement(By.id("answer")).getAttribute("class");
		System.out.println(color1);
		boolean f = true;
		while(f)
		{
			
			driver.switchTo().frame("child");
			String color2 = driver.findElement(By.id("answer")).getAttribute("class");
			System.out.println(color2);
			if(color1.equals(color2))
			{
				f = false;
			}
			else {
				driver.switchTo().defaultContent();
				driver.switchTo().frame("main");
				driver.findElement(By.linkText("Repaint Box 2")).click();
				
			}
		}
		driver.switchTo().defaultContent();
		driver.switchTo().frame("main");
		driver.findElement(By.linkText("Proceed")).click();
		Assert.assertEquals("http://10.0.1.86/tatoc/basic/drag",driver.getCurrentUrl());
		}
		
		
		@Test(dependsOnMethods= {"frame"})
		public void actiondrag() 
		{
			Assert.assertEquals(driver.findElement(By.xpath(".//*[@id='dragbox']")).isDisplayed(),true);
			Assert.assertEquals(driver.findElement(By.xpath(".//*[@id='dropbox']")).isDisplayed(),true);
		WebElement from = driver.findElement(By.xpath(".//*[@id='dragbox']"));
		WebElement to = driver.findElement(By.xpath(".//*[@id='dropbox']"));
		Actions builder = new Actions(driver);
		 
		Action dragAndDrop = builder.clickAndHold(from)
		 
		.moveToElement(to)
		 
		.release(to)
		 
		.build();
		 
		dragAndDrop.perform();
		driver.findElement(By.linkText("Proceed")).click();
		Assert.assertEquals("http://10.0.1.86/tatoc/basic/windows", driver.getCurrentUrl());
		}
		
		
		@Test(dependsOnMethods= {"actiondrag"})
		public void launchpopup() 
		{
		driver.findElement(By.linkText("Launch Popup Window")).click();
		
		String parentWindowHandler = driver.getWindowHandle(); // Store your parent window
		String subWindowHandler = null;

		Set<String> handles = driver.getWindowHandles(); // get all window handles
		Iterator<String> iterator = handles.iterator();
		while (iterator.hasNext()){
		    subWindowHandler = iterator.next();
		}
		driver.switchTo().window(subWindowHandler); // switch to popup window

		// Now you are in the popup window, perform necessary actions here
		WebElement send = driver.findElement(By.id("name"));
		send.sendKeys("Prince");
		driver.findElement(By.id("submit")).click();
		
		driver.switchTo().window(parentWindowHandler); 
		driver.findElement(By.linkText("Proceed")).click();
		Assert.assertEquals("http://10.0.1.86/tatoc/basic/cookie", driver.getCurrentUrl());
		}
		
		
		@Test(dependsOnMethods= {"launchpopup"})
		public void token() 
		{
		driver.findElement(By.linkText("Generate Token")).click();
		String token = driver.findElement(By.cssSelector("#token")).getText();
		
		String split = token.substring(7);
		Cookie token1 = new Cookie("Token", split);
	    driver.manage().addCookie(token1);
		
	    driver.findElement(By.linkText("Proceed")).click();
	    Assert.assertEquals("http://10.0.1.86/tatoc/end", driver.getCurrentUrl());
	}


}
