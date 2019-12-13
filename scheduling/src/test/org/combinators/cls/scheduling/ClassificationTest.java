package org.combinators.cls.scheduling;

import org.combinators.cls.scheduling.model.ShopClass;
import org.combinators.cls.scheduling.model.Task;
import org.combinators.cls.scheduling.utils.ClassificationUtils;
import org.combinators.cls.scheduling.utils.LoadSaveUtil;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ClassificationTest {

	@Test
	public void testClassificationFlowShopAscending() throws IOException {
		Task task = LoadSaveUtil.load(getClass().getResource("/flowShop4x4_ascending.task").getFile());

		ClassificationUtils.Classification classification = ClassificationUtils.classify(task);
		assertEquals(4, classification.getMachineCount());
		assertEquals(4, classification.getJobCount());
		assertEquals(ShopClass.FS, classification.getShopClass());
	}
	
	@Test
	public void testClassificationFlowShopNonAscending() throws IOException {
		Task task = LoadSaveUtil.load(getClass().getResource("/flowShop4x4_nonAscending.task").getFile());
		
		ClassificationUtils.Classification classification = ClassificationUtils.classify(task);
		assertEquals(4, classification.getMachineCount());
		assertEquals(4, classification.getJobCount());
		assertEquals(ShopClass.FS, classification.getShopClass());
	}
	
	@Test
	public void testClassificationJobShop() throws IOException {
		Task task = LoadSaveUtil.load(getClass().getResource("/jobShop4x4.task").getFile());
		
		ClassificationUtils.Classification classification = ClassificationUtils.classify(task);
		assertEquals(4, classification.getMachineCount());
		assertEquals(4, classification.getJobCount());
		assertEquals(ShopClass.JS, classification.getShopClass());
	}
	
	@Test
	public void testClassificationJobShop2() throws IOException {
		Task task = LoadSaveUtil.load(getClass().getResource("/jobShop3x5.task").getFile());
		
		ClassificationUtils.Classification classification = ClassificationUtils.classify(task);
		assertEquals(3, classification.getMachineCount());
		assertEquals(5, classification.getJobCount());
		assertEquals(ShopClass.JS, classification.getShopClass());
	}
	
	@Test
	public void testClassificationOpenShop() throws IOException {
		Task task = LoadSaveUtil.load(getClass().getResource("/openShop4x4.task").getFile());
		
		ClassificationUtils.Classification classification = ClassificationUtils.classify(task);
		assertEquals(4, classification.getMachineCount());
		assertEquals(4, classification.getJobCount());
		assertEquals(ShopClass.OS, classification.getShopClass());
	}
	
	@Test
	public void testClassificationFlexibleFlowShop() throws IOException {
		Task task = LoadSaveUtil.load(getClass().getResource("/flexibleFlowShop4x4_ascending.task").getFile());
		
		ClassificationUtils.Classification classification = ClassificationUtils.classify(task);
		assertEquals(5, classification.getMachineCount());
		assertEquals(4, classification.getJobCount());
		assertEquals(ShopClass.FFS, classification.getShopClass());
	}
	/*
	@Test
	public void testClassificationFlexibleJobShop() throws IOException {
		fail("Not yet implemented.");
	}*/
}