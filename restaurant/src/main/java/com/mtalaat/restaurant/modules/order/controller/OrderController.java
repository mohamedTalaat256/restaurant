package com.mtalaat.restaurant.modules.order.controller;

import com.mtalaat.restaurant.modules.auth.entity.User;
import com.mtalaat.restaurant.modules.auth.security.PermissionChecker;
import com.mtalaat.restaurant.modules.account.dto.JournalEntryDTO;
import com.mtalaat.restaurant.modules.order.dto.*;
import com.mtalaat.restaurant.modules.order.enums.OrderStatus;
import com.mtalaat.restaurant.modules.order.service.OrderService;
import com.mtalaat.restaurant.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final PermissionChecker permissionChecker;

    @Value("${app.menu.order-id}")
    private Long menuId;

    // ─────────────────────────────────────────────
    // CREATE
    // ─────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<ApiResponse> createOrder(@Valid @RequestBody CreateOrderRequestDto dto) {
        permissionChecker.checkCreate(menuId);
        User currentUser = permissionChecker.getCurrentUser();
        OrderDto created = orderService.createOrder(dto, currentUser);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_order_created", created, status.value()));
    }

    // ─────────────────────────────────────────────
    // READ
    // ─────────────────────────────────────────────

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        permissionChecker.checkRead(menuId);
        OrderDto order = orderService.getById(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_order_fetched", order, status.value()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAll(
            @RequestParam(required = false) OrderStatus orderStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        permissionChecker.checkRead(menuId);

        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<OrderDto> ordersPage = orderStatus != null
                ? orderService.getByStatus(pageable, orderStatus)
                : orderService.getAll(pageable);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_orders_fetched", ordersPage, status.value()));
    }

    // ─────────────────────────────────────────────
    // UPDATE ITEMS
    // ─────────────────────────────────────────────

    @PostMapping("/{id}/items")
    public ResponseEntity<ApiResponse> addItem(
            @PathVariable Long id,
            @Valid @RequestBody OrderItemRequestDto dto) {
        permissionChecker.checkEdit(menuId);
        OrderDto updated = orderService.addItem(id, dto);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_order_item_added", updated, status.value()));
    }

    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<ApiResponse> removeItem(
            @PathVariable Long id,
            @PathVariable Long itemId) {
        permissionChecker.checkEdit(menuId);
        OrderDto updated = orderService.removeItem(id, itemId);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_order_item_removed", updated, status.value()));
    }

    @PatchMapping("/{id}/items/{itemId}/quantity")
    public ResponseEntity<ApiResponse> updateItemQuantity(
            @PathVariable Long id,
            @PathVariable Long itemId,
            @RequestParam int quantity) {
        permissionChecker.checkEdit(menuId);
        OrderDto updated = orderService.updateItemQuantity(id, itemId, quantity);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_order_item_updated", updated, status.value()));
    }

    // ─────────────────────────────────────────────
    // DELETE
    // ─────────────────────────────────────────────

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        permissionChecker.checkDelete(menuId);
        orderService.delete(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_order_deleted", null, status.value()));
    }

    // ─────────────────────────────────────────────
    // MERGE / SPLIT
    // ─────────────────────────────────────────────

    @PostMapping("/merge")
    public ResponseEntity<ApiResponse> mergeOrders(@Valid @RequestBody MergeOrdersDto dto) {
        permissionChecker.checkEdit(menuId);
        OrderDto merged = orderService.mergeOrders(dto);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_orders_merged", merged, status.value()));
    }

    @PostMapping("/{id}/split")
    public ResponseEntity<ApiResponse> splitOrder(
            @PathVariable Long id,
            @Valid @RequestBody SplitOrderDto dto) {
        permissionChecker.checkEdit(menuId);
        List<OrderDto> splitOrders = orderService.splitOrder(id, dto);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_order_split", splitOrders, status.value()));
    }

    // ─────────────────────────────────────────────
    // COMPLETE & CHECKOUT
    // ─────────────────────────────────────────────

    @PostMapping("/{id}/complete")
    public ResponseEntity<ApiResponse> completeOrder(@PathVariable Long id) {
        permissionChecker.checkEdit(menuId);
        OrderDto completed = orderService.completeOrder(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_order_completed", completed, status.value()));
    }

    @PostMapping("/{id}/checkout")
    public ResponseEntity<ApiResponse> checkout(
            @PathVariable Long id,
            @Valid @RequestBody CheckoutDto dto) {
        permissionChecker.checkEdit(menuId);
        JournalEntryDTO journalEntry = orderService.checkout(id, dto);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_order_checked_out", journalEntry, status.value()));
    }

    // ─────────────────────────────────────────────
    // TRACKING
    // ─────────────────────────────────────────────

    @GetMapping("/{id}/tracking")
    public ResponseEntity<ApiResponse> tracking(@PathVariable Long id) {
        permissionChecker.checkRead(menuId);
        OrderTrackingDto tracking = orderService.getTracking(id);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_order_tracking_fetched", tracking, status.value()));
    }

    // ─────────────────────────────────────────────
    // PRINTER TEST
    // ─────────────────────────────────────────────

    @PostMapping("/print/test")
    public ResponseEntity<ApiResponse> testPrint(@Valid @RequestBody TestPrintRequestDto dto) {
        permissionChecker.checkRead(menuId);

        String body = (dto.getText() != null && !dto.getText().isBlank())
                ? dto.getText()
                : "هذه صفحة اختبار";

        List<String> lines = new ArrayList<>();
        lines.add(body);
        lines.add("");
        lines.add(dto.getIp() + ":" + dto.getPort());
        lines.add(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        try {
            byte[] pdf = buildA4Pdf("اختبار الطباعة", lines);
            sendToPrinter(dto.getIp(), dto.getPort(), pdf);

            HttpStatus status = HttpStatus.OK;
            return ResponseEntity.status(status)
                    .body(ApiResponse.success("msg_test_print_success", null, status.value()));

        } catch (IOException e) {
            HttpStatus status = HttpStatus.BAD_GATEWAY;
            return ResponseEntity.status(status)
                    .body(ApiResponse.error("msg_test_print_failed", e.getMessage(), status.value()));
        }
    }

    /**
     * Renders a single A4 page containing Arabic (right-to-left) text into a PDF.
     * Java2D performs Arabic shaping and bidirectional reordering automatically,
     * so the resulting page is a faithful raster that any laser printer can print.
     */
    private byte[] buildA4Pdf(String title, List<String> lines) throws IOException {
        // A4 at 150 DPI
        final int dpi = 150;
        final int width = (int) Math.round(8.27 * dpi);   // 1240 px
        final int height = (int) Math.round(11.69 * dpi);  // 1754 px
        final int margin = (int) Math.round(0.6 * dpi);

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        try {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);
            g.setColor(Color.BLACK);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            Font titleFont = pickArabicFont(Font.BOLD, 48);
            Font bodyFont = pickArabicFont(Font.PLAIN, 32);

            int y = margin;

            // Title (right-aligned)
            g.setFont(titleFont);
            FontMetrics tm = g.getFontMetrics();
            y += tm.getAscent();
            drawRightAligned(g, title, width - margin, y);
            y += tm.getDescent() + tm.getHeight();

            // Body lines (right-aligned)
            g.setFont(bodyFont);
            FontMetrics bm = g.getFontMetrics();
            for (String line : lines) {
                y += bm.getAscent();
                if (line != null && !line.isEmpty()) {
                    drawRightAligned(g, line, width - margin, y);
                }
                y += bm.getDescent() + (bm.getHeight() / 3);
            }
        } finally {
            g.dispose();
        }

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);
            PDImageXObject pdImage = LosslessFactory.createFromImage(doc, img);
            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                cs.drawImage(pdImage, 0, 0, PDRectangle.A4.getWidth(), PDRectangle.A4.getHeight());
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            doc.save(out);
            return out.toByteArray();
        }
    }

    private void drawRightAligned(Graphics2D g, String text, int rightX, int y) {
        int textWidth = g.getFontMetrics().stringWidth(text);
        g.drawString(text, rightX - textWidth, y);
    }

    /** Picks the first installed font able to render Arabic glyphs. */
    private Font pickArabicFont(int style, int size) {
        String sample = "أبجد هوز";
        for (String name : new String[]{"Arial", "Tahoma", "Segoe UI", "Noto Sans Arabic",
                "DejaVu Sans", "Dialog", Font.SANS_SERIF}) {
            Font f = new Font(name, style, size);
            if (f.canDisplayUpTo(sample) == -1) {
                return f;
            }
        }
        return new Font(Font.SANS_SERIF, style, size);
    }

    /** Streams the raw document bytes to a JetDirect (port 9100) network printer. */
    private void sendToPrinter(String ip, int port, byte[] data) throws IOException {
        try (Socket socket = new Socket(ip, port);
             OutputStream out = socket.getOutputStream()) {
            out.write(data);
            out.flush();
        }
    }
}