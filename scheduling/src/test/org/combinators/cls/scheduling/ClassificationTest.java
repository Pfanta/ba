package org.combinators.cls.scheduling;

import org.combinators.cls.scheduling.model.ShopClass;
import org.combinators.cls.scheduling.model.Task;
import org.combinators.cls.scheduling.utils.ClassificationUtils;
import org.combinators.cls.scheduling.utils.LoadSaveUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ClassificationTest {
	
	@Test
	public void testClassificationFlowShopAscending() throws IOException {
		Task task = LoadSaveUtil.load(getClass().getResource("/flowShop4x4_ascending.task").getFile());
		
		assertEquals(ClassificationUtils.classify(task).getShopClass(), ShopClass.FS);
	}
	
	@Test
	public void testClassificationFlowShopNonAscending() throws IOException {
		Task task = LoadSaveUtil.load(getClass().getResource("/flowShop4x4_nonAscending.task").getFile());
		
		assertEquals(ClassificationUtils.classify(task).getShopClass(), ShopClass.FS);
	}
	
	@Test
	public void testClassificationJobShop() throws IOException {
		Task task = LoadSaveUtil.load(getClass().getResource("/jobShop4x4.task").getFile());
		
		assertEquals(ClassificationUtils.classify(task).getShopClass(), ShopClass.JS);
	}
	
	@Test
	public void testClassificationOpenShop() throws IOException {
		fail("Not yet implemented.");
	}
	
	@Test
	public void testClassificationFlexibleFlowShop() throws IOException {
		fail("Not yet implemented.");
	}
	
	@Test
	public void testClassificationFlexibleJobShop() throws IOException {
		fail("Not yet implemented.");
	}
}