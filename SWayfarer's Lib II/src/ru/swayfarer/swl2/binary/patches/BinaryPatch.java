package ru.swayfarer.swl2.binary.patches;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import ru.swayfarer.swl2.binary.buffers.DynamicByteBuffer;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.json.JsonUtils;
import ru.swayfarer.swl2.json.NonJson;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.math.MathUtils;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.resource.rlink.ResourceLink;
import ru.swayfarer.swl2.resource.streams.DataInputStreamSWL;
import ru.swayfarer.swl2.resource.streams.DataOutputStreamSWL;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.threads.ThreadsUtils;
import ru.swayfarer.swl2.z.dependencies.com.nothome.delta.ByteBufferSeekableSource;
import ru.swayfarer.swl2.z.dependencies.com.nothome.delta.Delta;
import ru.swayfarer.swl2.z.dependencies.com.nothome.delta.GDiffPatcher;
import ru.swayfarer.swl2.z.dependencies.com.nothome.delta.RandomAccessFileSeekableSource;
import ru.swayfarer.swl2.z.dependencies.com.nothome.delta.SeekableSource;

/**
 * Бинарный патч 
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class BinaryPatch {
	
	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();

	/** Отметка о секции информации о патче */
	@InternalElement
	public static String PATCH_INFO_SECTION = "-- The patch info ";
	
	/** Отметка о секции контента патча */
	@InternalElement
	public static String PATCH_CONTENT_SECTION = "-- The patch content ";
	
	/** Информация о патче */
	@InternalElement
	public PatchInfo patchInfo = new PatchInfo();
	
	/** Функция, генерирующая контент патча */
	@InternalElement
	public IFunction1<BinaryPatch, DataInputStreamSWL> dataFun;
	
	/** Получить патч различий двух файлов */
	public static BinaryPatch valueOf(FileSWL source, FileSWL target)
	{
		try
		{
			BinaryPatch patch = new BinaryPatch();

			patch.patchInfo.targetSha256Hash = source.getHash(MathUtils.HASH_SHA_256);
			patch.setData(getDelta().compute(source, target.toInputStream()));
			
			return patch;
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while getting differences of bytes");
		}
		
		return null;
	}
	
	/** Задать контент патча */
	public <T extends BinaryPatch> T setData(FileSWL file)
	{
		return setData(file.toRlink());
	}
	
	/** Задать контент патча */
	public <T extends BinaryPatch> T setData(ResourceLink rlink)
	{
		this.dataFun = ResourceLinkSourceFun.of(rlink);
		
		return (T) this;
	}
	
	/** Задать контент патча */
	public <T extends BinaryPatch> T setData(byte[] bytes)
	{
		this.dataFun = ByteBufferSourceFun.of(bytes);
		
		return (T) this;
	}
	
	/** Задать контент патча */
	public <T extends BinaryPatch> T setData(DynamicByteBuffer buffer)
	{
		this.dataFun = ByteBufferSourceFun.of(buffer);
		
		return (T) this;
	}
	
	/** Получить патч с разницей двух наборов байт  */
	public static BinaryPatch valueOf(byte[] source, byte[] target)
	{
		try
		{
			BinaryPatch patch = new BinaryPatch();
			patch.setData(getDelta().compute(source, target));
			return patch;
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while getting differences of bytes");
		}
		
		return null;
	}
	
	/** Получить {@link Delta}, которая находит различия  */
	@InternalElement
	public static Delta getDelta()
	{
		return ThreadsUtils.getThreadLocal(() -> new Delta());
	}
	
	/** Получить патчер */
	@InternalElement
	public static GDiffPatcher getPatcher()
	{
		return ThreadsUtils.getThreadLocal(() -> new GDiffPatcher());
	}
	
	/** Применить патч */
	public <T extends BinaryPatch> T apply(ResourceLink target, OutputStream result)
	{
		SeekableSource source = null;
		FileSWL file = null;
		
		if ( (file = target.toFile()) != null)
		{
			try
			{
				String hash = patchInfo.targetSha256Hash;
				String fileHash = null;
				
				if (patchInfo != null)
				{
					if (!hash.equals(PatchInfo.NO_HASH_KEY) && !hash.equals(fileHash = file.getHash(MathUtils.HASH_SHA_256)))
					{
						logger.error("Target's hash", fileHash, "is not equals to patch target hash", hash);
					}
				}
				
				source = new RandomAccessFileSeekableSource(new RandomAccessFile(file, "r"));
			}
			catch (Throwable e)
			{
				logger.error(e, "Error while getting patch", this, "source"); 
				return (T) this;
			}
		}
		else
		{
			source = new ByteBufferSeekableSource(target.toBytes());
		}
		
		final SeekableSource seekableSource = source;
		
		logger.safe(() -> {
			getPatcher().patch(seekableSource, dataFun.apply(this), result);
		}, "Error while patching", target, "by", this);
		
		
		return (T) this;
	}
	
	/** Безопасно сохранить патч в файл */
	public <T extends BinaryPatch> T saveToSafe(FileSWL file)
	{
		try 
		{
			return saveTo(file.toOutputStream(), true);
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while saving patch", this, "to file");
		}
		
		return (T) this;
	}
	
	/** Безопасно загрущить патч из файла*/
	public static BinaryPatch loadFromSafe(InputStream is)
	{
		try
		{
			return loadFrom(is);
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while loading patch from", is);
		}
		
		return null;
	}
	
	/** Безопасно загрущить патч из файла*/
	public static BinaryPatch loadFrom(InputStream is) throws IOException
	{
		BinaryPatch ret = new BinaryPatch();
		
		DataInputStreamSWL dis = DataInputStreamSWL.of(is);
		dis.setEncoding("UTF-8");
		dis.setLineSplitter(StringUtils.LF);
		
		int readedLinesCount = 0;
		
		StringBuilder sb = new StringBuilder();
		
		int available = dis.available();
		
		while (readedLinesCount < 3 && dis.hasNextByte())
		{
			String ln = dis.readStringLn();
			
			if (!ln.startsWith("--"))
				sb.append(ln);
			
			if (ln.equals(PATCH_CONTENT_SECTION))
				break;
		}
		
		ret.patchInfo.infoBytesCount = available - dis.available();
		
		String patchInfoString = sb.toString();
		
		if (!StringUtils.isEmpty(patchInfoString))
		{
			ret.patchInfo = JsonUtils.loadFromJson(patchInfoString, PatchInfo.class);
		}
		
		ret.dataFun = ByteBufferSourceFun.of(dis.readAll());
		
		return ret;
	}
	
	/** Сохранить патч в файл */
	public <T extends BinaryPatch> T saveTo(FileSWL file) throws IOException
	{
		return saveTo(file.toOutputStream(), true);
	}
	
	/** Сохранить патч в поток  */
	public <T extends BinaryPatch> T saveTo(OutputStream os, boolean close) throws IOException
	{
		DataOutputStreamSWL dos = DataOutputStreamSWL.of(os);
		dos.setLineSplitter(StringUtils.LF);
		dos.setEncoding("UTF-8");
		
		if (patchInfo != null)
		{
			dos.writeLn(PATCH_INFO_SECTION);
			dos.writeLn(JsonUtils.saveToJson(patchInfo));
		}
		
		dos.writeLn(PATCH_CONTENT_SECTION);
		dos.write(dataFun.apply(this).readAll());
		
		if (close)
			dos.closeSafe();
		
		return (T) this;
	}

	/**
	 * Информация о патче
	 * @author swayfarer
	 */
	@InternalElement
	public static class PatchInfo {
		
		/** Значение, которое подставляется вместо отсутствующего хэша */
		public static String NO_HASH_KEY = "no hash";
		
		/** Имя и версия патча */
		public String name = "Patch name", vesrion= "Patch version";
		
		/** Хэш цели, чтобы не наделать дел */
		public String targetSha256Hash = NO_HASH_KEY;
		
		/** Кол-во байт, которое занимает этот заголовок (Расчитывается при чтении) */
		@NonJson
		public int infoBytesCount;
		
	}

	@InternalElement
	@AllArgsConstructor(staticName = "of")
	public static class ByteBufferSourceFun implements IFunction1<BinaryPatch, DataInputStreamSWL> {
		
		@NonNull
		public DynamicByteBuffer wrappedBuffer;

		@Override
		public DataInputStreamSWL apply(BinaryPatch patch)
		{
			return wrappedBuffer.toInputStream();
		}
		
		public static ByteBufferSourceFun of(byte[] bytes)
		{
			return of(DynamicByteBuffer.wrap(bytes));
		}
		
	}

	@InternalElement
	@AllArgsConstructor(staticName = "of")
	public static class ResourceLinkSourceFun implements IFunction1<BinaryPatch, DataInputStreamSWL> {

		@NonNull
		public ResourceLink rlink;
		
		@Override
		public DataInputStreamSWL apply(BinaryPatch patch)
		{
			return rlink.toStream().forwardSafe(patch.patchInfo.infoBytesCount);
		}
		
	}
}

