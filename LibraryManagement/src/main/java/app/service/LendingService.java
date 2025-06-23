//package service;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.OptimisticLockingFailureException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import entity.Item;
//import repository.MemberRepository;
//
//@Transactional
//@Service
//public class LendingService {
//
//	@Autowired
//	private MemberRepository itemRepository;
//
//	public Item insert(Item item) {
//		item.setVersion(1);
//		Item result =itemRepository.save(item);
//		return result;
//	}
//
//	public Item getItem(Integer id) {
//		Item item = itemRepository.findById(id).orElse(null);
//		return item;
//	}
//
//	public void update(Item item) throws Exception {
//
//		try {
//			itemRepository.save(item);
//			itemRepository.flush();
//		} catch (OptimisticLockingFailureException e) {
//			throw new Exception("optimistic.locking.error");
//		}
//
//	}
//
//	public void delete(Integer id) {
//		Item target = getItem(id);
//		itemRepository.delete(target);
//	}
//
//	public List<Item> selectAll() {
//		List<Item> itemList = itemRepository.findAll();
//		return itemList;
//	}
//
//}
