package com.media.catalog.io;

import com.media.catalog.domain.Author;
import com.media.catalog.domain.ContentStatus;
import com.media.catalog.domain.MediaItem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MediaFileIO {

    public void writeToFile(List<MediaItem> items, String fileName) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (MediaItem item : items) {
                // simple CSV: id;title;authorId;authorName;status
                writer.printf("%s;%s;%s;%s;%s%n",
                        item.id(), item.title(), item.author().id(), item.author().name(), item.status());
            }
        }
    }

    public List<MediaItem> readFromFile(String fileName) throws IOException {
        List<MediaItem> items = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                Author author = new Author(parts[2], parts[3]);
                items.add(new MediaItem(parts[0], parts[1], author, ContentStatus.valueOf(parts[4])));
            }
        }
        return items;
    }
}