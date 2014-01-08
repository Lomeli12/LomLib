package net.lomeli.lomlib.item;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class CustomBookUtil {

    public static ItemStack createNewBook(String author, String title, String[] pageText) {
        ItemStack newBook = new ItemStack(Items.written_book);
        NBTUtil.setString(newBook, "author", author);
        NBTUtil.setString(newBook, "title", title);
        NBTTagList pages = newBook.getTagCompound().func_150295_c("pages", pageText.length);
        for(int i = 0; i < pageText.length; i++) {
            pages.appendTag(new NBTTagString(pageText[i]));
        }
        newBook.setTagInfo("pages", pages);
        return newBook;
    }
}
