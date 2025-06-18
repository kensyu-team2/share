package jp.co.systempack.itemManagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.systempack.itemManagement.entity.Item;
import jp.co.systempack.itemManagement.exception.ApplicationException;
import jp.co.systempack.itemManagement.repository.ItemRepository;

@Transactional
@Service
public class ItemService {

	@Autowired
	private ItemRepository itemRepository;

	public Item insert(Item item) {
		item.setVersion(1);
		Item result =itemRepository.save(item);
		return result;
	}

	public Item getItem(Integer id) {
		Item item = itemRepository.findById(id).orElse(null);
		return item;
	}

	public void update(Item item) {

		try {
			itemRepository.save(item);
			itemRepository.flush();
		} catch (OptimisticLockingFailureException e) {
			throw new ApplicationException("optimistic.locking.error");
		}

	}

	public void delete(Integer id) {
		Item target = getItem(id);
		itemRepository.delete(target);
	}

	public List<Item> selectAll() {
		List<Item> itemList = itemRepository.findAll();
		return itemList;
	}

}
