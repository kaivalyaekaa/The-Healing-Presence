package in.thehealingpresence.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Dev-only static-asset bridge. For every /images/{filename} request:
 *   - serves the real file from classpath:/static/images/ if present,
 *   - otherwise renders a gold-tinted PNG placeholder with the inferred label.
 *
 * Prod uses Spring's regular cached static-resource handler instead (this bean
 * is gated on the dev profile).
 */
@RestController
@Profile("dev")
public class DevImageFallbackController {

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<byte[]> serveImage(@PathVariable("filename") String filename) throws Exception {
        ClassPathResource real = new ClassPathResource("static/images/" + filename);
        if (real.exists()) {
            try (InputStream in = real.getInputStream()) {
                byte[] bytes = in.readAllBytes();
                return ResponseEntity.ok()
                        .contentType(guessType(filename))
                        .cacheControl(CacheControl.noCache())
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                        .body(bytes);
            }
        }
        byte[] png = renderPlaceholder(prettyLabel(filename));
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .cacheControl(CacheControl.noCache())
                .body(png);
    }

    private static byte[] renderPlaceholder(String label) throws Exception {
        int width = 1200;
        int height = 800;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // gold-soft background (#f5ebcf)
            g.setColor(new Color(0xf5, 0xeb, 0xcf));
            g.fillRect(0, 0, width, height);

            // subtle gold border
            g.setColor(new Color(0xe3, 0xc8, 0x78));
            g.setStroke(new java.awt.BasicStroke(8f));
            g.drawRect(20, 20, width - 40, height - 40);

            // small subtitle on top
            g.setColor(new Color(0xa3, 0x7f, 0x25, 200));
            g.setFont(new Font(Font.SERIF, Font.ITALIC, 28));
            String subtitle = "The Healing Presence — placeholder image";
            int subWidth = g.getFontMetrics().stringWidth(subtitle);
            g.drawString(subtitle, (width - subWidth) / 2, height / 2 - 90);

            // big label, centred
            g.setColor(new Color(0xa3, 0x7f, 0x25));
            Font labelFont = new Font(Font.SERIF, Font.PLAIN, 84);
            g.setFont(labelFont);
            TextLayout layout = new TextLayout(label, labelFont, g.getFontRenderContext());
            Rectangle2D b = layout.getBounds();
            float x = (float) ((width - b.getWidth()) / 2 - b.getX());
            float y = (float) (height / 2.0 + b.getHeight() / 2 - b.getY() - b.getHeight());
            layout.draw(g, x, y);

            // hint to user
            g.setColor(new Color(0xa3, 0x7f, 0x25, 160));
            g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 22));
            String hint = "Save a real photo at /static/images/ to replace this";
            int hintWidth = g.getFontMetrics().stringWidth(hint);
            g.drawString(hint, (width - hintWidth) / 2, height / 2 + 120);
        } finally {
            g.dispose();
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(img, "PNG", out);
        return out.toByteArray();
    }

    /** "home-hero.jpg" → "Home Hero", "about-space.png" → "About Space". */
    private static String prettyLabel(String filename) {
        String base = filename;
        int dot = base.lastIndexOf('.');
        if (dot > 0) base = base.substring(0, dot);
        String[] parts = base.split("[-_]");
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            if (p.isEmpty()) continue;
            if (sb.length() > 0) sb.append(' ');
            sb.append(Character.toUpperCase(p.charAt(0)));
            if (p.length() > 1) sb.append(p.substring(1));
        }
        return sb.toString();
    }

    private static MediaType guessType(String filename) {
        String f = filename.toLowerCase();
        if (f.endsWith(".png")) return MediaType.IMAGE_PNG;
        if (f.endsWith(".jpg") || f.endsWith(".jpeg")) return MediaType.IMAGE_JPEG;
        if (f.endsWith(".gif")) return MediaType.IMAGE_GIF;
        if (f.endsWith(".webp")) return MediaType.parseMediaType("image/webp");
        if (f.endsWith(".svg")) return MediaType.parseMediaType("image/svg+xml");
        return MediaType.APPLICATION_OCTET_STREAM;
    }
}
