package net.lomeli.lomlib.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class CustomBookUtil {

    public static ItemStack createNewBook(String author, String title, String[] pageText) {
        ItemStack newBook = new ItemStack(Item.writtenBook);
        NBTUtil.setString(newBook, "author", author);
        NBTUtil.setString(newBook, "title", title);
        NBTTagList pages = new NBTTagList();
        for (int i = 0; i < pageText.length; i++) {
            pages.appendTag(new NBTTagString("" + (i + 1), pageText[i]));
        }
        newBook.setTagInfo("pages", pages);
        return newBook;
    }
}
