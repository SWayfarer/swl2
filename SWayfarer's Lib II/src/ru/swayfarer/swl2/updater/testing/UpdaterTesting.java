package ru.swayfarer.swl2.updater.testing;

import java.util.HashMap;
import java.util.Map;

import ru.swayfarer.swl2.app.ApplicationSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.resource.rlink.RLUtils;
import ru.swayfarer.swl2.swconf.utils.SwconfSerializationHelper;
import ru.swayfarer.swl2.updater.DownloadHashUpdater;
import ru.swayfarer.swl2.updater.UpdaterInfo;

public class UpdaterTesting extends ApplicationSWL {

	public static void main(String[] args)
	{
		startApplication(args);
	}
	
	@Override
	public void startSafe(IExtendedList<String> args) throws Throwable
	{
//		DownloadHashUpdater updater = new DownloadHashUpdater();
//		UpdaterInfo updaterInfo = new UpdaterInfo();
//		updater.updaterInfo = updaterInfo;
//		UpdateContent updateContent = new UpdateContent();
//		updaterInfo.content = updateContent;
//		updateContent.hashingType = MathUtils.HASH_MD5;
//		
		FileSWL remoteRootFile = new FileSWL("/tmp/updaterswl/remote/");
		FileSWL localRootFile = new FileSWL("/tmp/updaterswl/local/");
		
//		logger.info(localRootFile.getSubfiles());
		
//		updater.refresh(localRootFile, new FileUpdaterRemover(remoteRootFile), new FileUpdaterUploader(remoteRootFile));
	
//		GsonSerializationFuns.init();
//		String json = JsonUtils.saveToJson(new TestModel());
//		
//		logger.info(json);
		
//		String str = SwconfSerializationHelper.standart.saveToSwconf(updater.updaterInfo);
//		logger.info(str);
		
//		logger.info(updaterInfo.content.files.size());
//		
//		TestModel model2 = JsonUtils.loadFromJson(json, TestModel.class);
//		
//		logger.info(model2.map);
		
//		SwconfPrimitive primitive = SwconfSerializationHelper.standart.saveToSwconfPrimitive(updaterInfo);
		
//		FileSWL file = new FileSWL("out.swconf").createIfNotFoundSafe().setData(str.getBytes());
//		
		String singleString = RLUtils.toSingleString("f:out.swconf");
//		
//		logger.info(singleString);
		
		DownloadHashUpdater updater = new DownloadHashUpdater();
		
		UpdaterInfo updaterInfo = SwconfSerializationHelper.standart.readFromSwconf(singleString, UpdaterInfo.class);
		
		updater.updaterInfo = updaterInfo;
		
		updater.update(localRootFile, (root, f) -> f.removeIfExists());
		
//		logger.info(updaterInfo.content.files.size());
		
//		logger.info(primitive.toString(0));
		
//		logger.info(SwconfSerializationHelper.standart.saveToSwconf(updaterInfo));
	}
	
	public static class TestKey {
		public int a, b;
	}
	
	public static class TestValue {
		public String c = "\",C";
		public String d = "D";
	}
	
	public static class TestModel {
		
		public Map<TestKey, TestValue> map = new HashMap<>();
		
		public TestModel()
		{
			map.put(new TestKey(), new TestValue());
		}
	}
	
}
