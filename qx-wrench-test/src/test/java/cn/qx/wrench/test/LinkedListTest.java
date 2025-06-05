package cn.qx.wrench.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import cn.qx.wrench.design.framework.link.model2.chain.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LinkedList单元测试类
 * 
 * @author qinxiao
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("LinkedList测试")
public class LinkedListTest {

    private LinkedList<String> linkedList;
    private final String TEST_NAME = "测试链表";
    private final String ITEM_A = "A";
    private final String ITEM_B = "B";
    private final String ITEM_C = "C";
    private final String NON_EXISTENT_ITEM = "Z";

    @BeforeEach
    void setUp() {
        // 初始化测试对象
        linkedList = new LinkedList<>(TEST_NAME);
    }

    @Test
    @DisplayName("测试构造函数和getName方法")
    void constructor_Success() {
        // Arrange - 在setUp中已完成

        // Act & Assert
        assertEquals(TEST_NAME, linkedList.getName(), "链表名称应该与构造函数中提供的名称一致");
    }

    @Test
    @DisplayName("测试add方法添加元素")
    void add_Success() {
        // Arrange - 在setUp中已完成

        // Act
        boolean result = linkedList.add(ITEM_A);

        // Assert
        assertTrue(result, "add方法应返回true表示添加成功");
        assertEquals(ITEM_A, linkedList.get(0), "添加的元素应该可以通过索引0获取");
    }

    @Test
    @DisplayName("测试addFirst方法添加元素到链表头部")
    void addFirst_Success() {
        // Arrange
        linkedList.add(ITEM_A); // 先添加一个元素

        // Act
        boolean result = linkedList.addFirst(ITEM_B);

        // Assert
        assertTrue(result, "addFirst方法应返回true表示添加成功");
        assertEquals(ITEM_B, linkedList.get(0), "通过addFirst添加的元素应该位于索引0");
        assertEquals(ITEM_A, linkedList.get(1), "原来的第一个元素应该变成第二个元素");
    }

    @Test
    @DisplayName("测试addLast方法添加元素到链表尾部")
    void addLast_Success() {
        // Arrange
        linkedList.add(ITEM_A); // 先添加一个元素

        // Act
        boolean result = linkedList.addLast(ITEM_B);

        // Assert
        assertTrue(result, "addLast方法应返回true表示添加成功");
        assertEquals(ITEM_A, linkedList.get(0), "第一个元素应该保持不变");
        assertEquals(ITEM_B, linkedList.get(1), "通过addLast添加的元素应该位于链表末尾");
    }

    @Test
    @DisplayName("测试remove方法移除存在的元素")
    void remove_ExistingElement_Success() {
        // Arrange
        linkedList.add(ITEM_A);
        linkedList.add(ITEM_B);
        linkedList.add(ITEM_C);

        // Act
        boolean result = linkedList.remove(ITEM_B);

        // Assert
        assertTrue(result, "remove方法应返回true表示移除成功");
        assertEquals(2, getSize(), "移除一个元素后，链表大小应该减少1");
        assertEquals(ITEM_A, linkedList.get(0), "第一个元素应该保持不变");
        assertEquals(ITEM_C, linkedList.get(1), "移除中间元素后，后面的元素应该前移");
    }

    @Test
    @DisplayName("测试remove方法移除不存在的元素")
    void remove_NonExistingElement_ReturnsFalse() {
        // Arrange
        linkedList.add(ITEM_A);

        // Act
        boolean result = linkedList.remove(NON_EXISTENT_ITEM);

        // Assert
        assertFalse(result, "remove方法应返回false表示未找到要移除的元素");
        assertEquals(1, getSize(), "链表大小应该保持不变");
    }

    @Test
    @DisplayName("测试remove方法移除null元素")
    void remove_NullElement_Success() {
        // Arrange
        linkedList.add(null);
        linkedList.add(ITEM_A);

        // Act
        boolean result = linkedList.remove(null);

        // Assert
        assertTrue(result, "remove方法应返回true表示成功移除null元素");
        assertEquals(1, getSize(), "移除一个元素后，链表大小应该减少1");
        assertEquals(ITEM_A, linkedList.get(0), "移除null元素后，后面的元素应该前移");
    }

    @Test
    @DisplayName("测试get方法获取有效索引的元素")
    void get_ValidIndex_ReturnsElement() {
        // Arrange
        linkedList.add(ITEM_A);
        linkedList.add(ITEM_B);

        // Act & Assert
        assertEquals(ITEM_A, linkedList.get(0), "get方法应返回索引0处的元素");
        assertEquals(ITEM_B, linkedList.get(1), "get方法应返回索引1处的元素");
    }

    @Test
    @DisplayName("测试get方法使用无效索引抛出异常")
    void get_InvalidIndex_ThrowsException() {
        // Arrange
        linkedList.add(ITEM_A);

        // Act & Assert
        assertThrows(IndexOutOfBoundsException.class, () -> linkedList.get(-1), "使用负索引应抛出IndexOutOfBoundsException");
        assertThrows(IndexOutOfBoundsException.class, () -> linkedList.get(1), "使用超出链表大小的索引应抛出IndexOutOfBoundsException");
    }

    @Test
    @DisplayName("测试空链表的行为")
    void emptyList_Behavior() {
        // Act & Assert
        assertEquals(0, getSize(), "新创建的链表大小应为0");
        assertThrows(IndexOutOfBoundsException.class, () -> linkedList.get(0), "空链表获取元素应抛出异常");
        assertFalse(linkedList.remove(ITEM_A), "空链表移除不存在的元素应返回false");
    }

    @Test
    @DisplayName("测试多次添加和删除元素后链表的完整性")
    void multipleOperations_MaintainsIntegrity() {
        // Arrange & Act
        linkedList.add(ITEM_A);       // [A]
        linkedList.addFirst(ITEM_B);   // [B, A]
        linkedList.addLast(ITEM_C);    // [B, A, C]
        linkedList.remove(ITEM_A);     // [B, C]
        linkedList.addFirst(ITEM_A);   // [A, B, C]

        // Assert
        assertEquals(3, getSize(), "经过多次操作后，链表大小应该正确");
        assertEquals(ITEM_A, linkedList.get(0), "第一个元素应该是A");
        assertEquals(ITEM_B, linkedList.get(1), "第二个元素应该是B");
        assertEquals(ITEM_C, linkedList.get(2), "第三个元素应该是C");
    }

    /**
     * 获取链表大小的辅助方法
     * 由于size字段是包级私有的，我们通过反射获取它的值
     */
    private int getSize() {
        try {
            java.lang.reflect.Field sizeField = LinkedList.class.getDeclaredField("size");
            sizeField.setAccessible(true);
            return (int) sizeField.get(linkedList);
        } catch (Exception e) {
            fail("获取size字段失败: " + e.getMessage());
            return -1; // 不会执行到这里
        }
    }
}