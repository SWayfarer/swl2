package ru.swayfarer.swl2.collections.weak;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import lombok.Synchronized;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.collections.streams.DataStream;
import ru.swayfarer.swl2.markers.InternalElement;

/**
 * "Слабый" лист, который держит слабые ссылки на свои элементы 
 * @author swayfarer
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class WeakList<Element_Type> implements IExtendedList<Element_Type> {

	/** Был ли лист модифицирован? */
	@InternalElement
	public Boolean isModified = false;
	
	/** Лист ссылок */
	@InternalElement
	public IExtendedList<WeakReference<Element_Type>> referencesList = CollectionsSWL.createExtendedList();
	
	@Synchronized(value = "isModified")
	public void removeNullReferences()
	{
		if (!isModified)
			return;
		
		List<WeakReference<Element_Type>> toRemove = new ArrayList<WeakReference<Element_Type>>();
		
		for (WeakReference<Element_Type> ref : referencesList)
		{
			if (ref.get() == null)
				toRemove.add(ref);
		}
		
		referencesList.removeAll(toRemove);
		
		isModified = false;
	}
	
	/** Получить лист, состоящий не из ссылкок на элементы, а из самих элементов*/
	public IExtendedList<Element_Type> asNonReferenceList()
	{
		return referencesList.dataStream().mapped((link) -> link.get()).toList();
	}
	
	/** Создать лист ссылок на элементы коллекции */
	public IExtendedList<WeakReference<Element_Type>> createRefsList(Collection<?> c)
	{
		return ReflectionUtils.forceCast(DataStream.of(c)
				.mapped((e) -> new WeakReference<>(e))
				.toList());
				
	}

	/**
	 * Получить ссылку на элемент
	 * @param element Элемент, на который создается ссылка
	 * @return Ссылка на элемент
	 */
	public WeakReference<Element_Type> createRef(Object element)
	{
		return new WeakReference(element);
	}
	
	@Synchronized(value = "isModified")
	public void onModify()
	{
		removeNullReferences();
		isModified = true;
	}
	
	@Override
	public int size()
	{
		removeNullReferences();
		return referencesList.size();
	}

	@Override
	public boolean isEmpty()
	{
		removeNullReferences();
		return referencesList.isEmpty();
	}

	@Override
	public boolean contains(Object o)
	{
		removeNullReferences();
		return false;
	}

	@Override
	public Object[] toArray()
	{
		removeNullReferences();
		
		Object[] ret = new Object[referencesList.size()];
		int nextId = 0;
		
		for (WeakReference<Element_Type> ref : referencesList)
		{
			ret[nextId++] = ref.get();
		}
		
		return ret;
	}

	@Override
	public <T> T[] toArray(T[] a)
	{
		removeNullReferences();
		
		if (a.length < size())
		{
			a = CollectionsSWL.createArray(ReflectionUtils.cleanFromArray(a.getClass()), size());
		}
		
		int nextId = 0;
		
		for (WeakReference<Element_Type> ref : referencesList)
			a[nextId ++] = (T) ref.get();
		
		return a;
	}

	@Override
	public boolean add(Element_Type e)
	{
		onModify();
		return referencesList.add(new WeakReference<Element_Type>(e));
	}
	
	@Override
	public boolean remove(Object o)
	{
		onModify();
		return referencesList.remove(createRef(o));
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		removeNullReferences();
		return referencesList.containsAll(createRefsList(c));
	}

	@Override
	public boolean addAll(Collection<? extends Element_Type> c)
	{
		onModify();
		return referencesList.addAll(createRefsList(c));
	}

	@Override
	public boolean addAll(int index, Collection<? extends Element_Type> c)
	{
		onModify();
		return referencesList.addAll(index, createRefsList(c));
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		onModify();
		return referencesList.removeAll(createRefsList(c));
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		onModify();
		return referencesList.retainAll(createRefsList(c));
	}

	@Override
	public void clear()
	{
		isModified = false;
		referencesList.clear();
	}

	@Override
	public Element_Type get(int index)
	{
		removeNullReferences();
		return referencesList.get(index).get();
	}

	@Override
	public Element_Type set(int index, Element_Type element)
	{
		onModify();
		
		Element_Type prev = referencesList.get(index).get();
		
		referencesList.set(index, createRef(element));
		
		return prev;
	}

	@Override
	public void add(int index, Element_Type element)
	{
		onModify();
		referencesList.add(index, createRef(element));
	}

	@Override
	public Element_Type remove(int index)
	{
		onModify();
		Element_Type prev = referencesList.get(index).get();
		referencesList.remove(index);
		return prev;
	}

	@Override
	public int indexOf(Object o)
	{
		removeNullReferences();
		return referencesList.indexOf(createRef(o));
	}

	@Override
	public int lastIndexOf(Object o)
	{
		removeNullReferences();
		return referencesList.indexOf(createRef(o));
	}

	@Override
	public ListIterator<Element_Type> listIterator()
	{
		removeNullReferences();
		return asNonReferenceList().listIterator();
	}

	@Override
	public ListIterator<Element_Type> listIterator(int index)
	{
		removeNullReferences();
		return asNonReferenceList().listIterator(index);
	}

	@Override
	public List<Element_Type> subList(int fromIndex, int toIndex)
	{
		removeNullReferences();
		return asNonReferenceList().subList(fromIndex, toIndex);
	}

	@Override
	public Iterator<Element_Type> getConcurrentIterator()
	{
		removeNullReferences();
		return asNonReferenceList().iterator();
	}

	@Override
	public Iterator<Element_Type> iterator()
	{
		removeNullReferences();
		return getConcurrentIterator();
	}

	@Override
	public boolean isConcurrent()
	{
		return true;
	}

	@Override
	public <T extends IExtendedList<Element_Type>> T setConcurrent(boolean isConcurrent)
	{
		return (T) this;
	}

}
