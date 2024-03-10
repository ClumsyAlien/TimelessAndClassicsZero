package com.tac.guns.resource.loader;

import com.tac.guns.GunMod;
import com.tac.guns.resource.CommonAssetManager;
import com.tac.guns.resource.CommonGunPackLoader;
import com.tac.guns.resource.pojo.ammo.BulletData;
import com.tac.guns.resource.pojo.gun.GunData;
import com.tac.guns.util.TacPathVisitor;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class AmmoDataLoader {
    private static final Marker MARKER = MarkerManager.getMarker("BulletDataLoader");
    private static final Pattern DATA_PATTERN = Pattern.compile("^(\\w+)/ammo/data/([\\w/]+)\\.json$");

    public static boolean load(ZipFile zipFile, String zipPath) throws IOException {
        Matcher matcher = DATA_PATTERN.matcher(zipPath);
        if (matcher.find()) {
            String namespace = matcher.group(1);
            String path = matcher.group(2);
            ZipEntry entry = zipFile.getEntry(zipPath);
            if (entry == null) {
                GunMod.LOGGER.warn(MARKER, "{} file don't exist", zipPath);
                return false;
            }
            try (InputStream stream = zipFile.getInputStream(entry)) {
                ResourceLocation registryName = new ResourceLocation(namespace, path);
                BulletData data = CommonGunPackLoader.GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), BulletData.class);
                CommonAssetManager.INSTANCE.putBulletData(registryName, data);
                return true;
            }
        }
        return false;
    }

    public static void load(File root) throws IOException {
        Path filePath = root.toPath().resolve("ammo/data");
        if (Files.isDirectory(filePath)) {
            TacPathVisitor visitor = new TacPathVisitor(filePath.toFile(), root.getName(), ".json", (id, file) -> {
                try (InputStream stream = Files.newInputStream(file)) {
                    BulletData data = CommonGunPackLoader.GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), BulletData.class);
                    CommonAssetManager.INSTANCE.putBulletData(id, data);
                } catch (IOException exception) {
                    GunMod.LOGGER.warn(MARKER, "Failed to read data file: {}", file);
                    exception.printStackTrace();
                }
            });
            Files.walkFileTree(filePath, visitor);
        }
    }
}
