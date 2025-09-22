package com.fpt.etc.helpers;

import com.fpt.etc.entity.*;

import java.util.List;
import java.util.stream.Collectors;

public class EmailTemplateHelper {

    public static String generateBookingConfirmationEmail(
            Booking booking,
            Event ev,
            Venue venue,
            Room room,
            Menu menu,
            List<Dish> dishes,
            List<Service> services
    ) {
        String serviceNames = services.stream()
                .map(Service::getName)
                .collect(Collectors.joining(", "));

        String dishHtmlList = dishes.stream()
                .map(dish -> "<span style='display:inline-block;background:#fff;padding:4px 8px;margin:2px;border-radius:4px;font-size:14px;'>"
                        + dish.getName() + "</span>")
                .collect(Collectors.joining());

        String statusColor = "#6b7280";
        String statusLabel = "🕐 Chờ xử lý";
        String nextStepMessage = "Chúng tôi sẽ sớm liên hệ với bạn để xác nhận đơn hàng.";

        if (booking.getStatus() != null) {
            switch (booking.getStatus().toLowerCase()) {
                case "pending":
                    statusColor = "#f59e0b";
                    statusLabel = "🕐 Chờ xử lý";
                    nextStepMessage = "Chúng tôi sẽ sớm liên hệ với bạn trong vòng 24 giờ để xác nhận đơn hàng.";
                    break;
                case "confirmed":
                    statusColor = "#3b82f6";
                    statusLabel = "✅ Đã xác nhận";
                    nextStepMessage = "Đơn hàng của bạn đã được xác nhận. Vui lòng tiến hành thanh toán để chúng tôi giữ chỗ.";
                    break;
                case "paid":
                    statusColor = "#10b981";
                    statusLabel = "💰 Đã thanh toán";
                    nextStepMessage = "Đơn hàng đã được thanh toán. Chúng tôi sẽ chuẩn bị mọi thứ cho sự kiện của bạn.";
                    break;
                case "cancelled":
                    statusColor = "#ef4444";
                    statusLabel = "❌ Đã hủy";
                    nextStepMessage = "Đơn hàng đã bị hủy. Nếu có bất kỳ thay đổi nào, vui lòng liên hệ lại với chúng tôi.";
                    break;
            }
        }

        return String.format("""
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset='utf-8'>
                    <style>
                        body { font-family:Segoe UI,Tahoma,Geneva,Verdana,sans-serif; background:#f4f4f4; color:#333; }
                        .container { max-width:600px;margin:20px auto;background:white;padding:30px;border-radius:12px;box-shadow:0 4px 6px rgba(0,0,0,0.1); }
                        .header { background:#667eea;color:white;padding:20px;border-radius:8px 8px 0 0;text-align:center; }
                        .info-row { display:flex;justify-content:space-between;padding:6px 0;border-bottom:1px solid #eee; }
                        .info-label { font-weight:bold; }
                        .info-value { text-align:right; }
                        .next-step { background:#e7f3ff;padding:15px;border-left:4px solid #0056b3;margin-top:20px;border-radius:4px; }
                    </style>
                </head>
                <body>
                    <div class='container'>
                        <div class='header'>
                            <h2>CaterEase - Đặt tiệc thành công</h2>
                        </div>
                        <p>Xin chào <strong>%s</strong>,</p>
                        <p>Bạn đã đặt tiệc thành công trên hệ thống CaterEase. Dưới đây là thông tin chi tiết:</p>

                        <div class='info-row'><span class='info-label'>Mã đơn hàng:</span><span class='info-value'>%s</span></div>
                        <div class='info-row'><span class='info-label'>Trạng thái:</span><span class='info-value' style='color:%s'>%s</span></div>
                        <div class='info-row'><span class='info-label'>Ngày tổ chức:</span><span class='info-value'>%s lúc %s</span></div>
                        <div class='info-row'><span class='info-label'>Sự kiện:</span><span class='info-value'>%s</span></div>
                        <div class='info-row'><span class='info-label'>Địa điểm:</span><span class='info-value'>%s - %s</span></div>
                        <div class='info-row'><span class='info-label'>Thực đơn:</span><span class='info-value'>%s (%s VND)</span></div>

                        <h4 style='margin-top:20px;'>🍽️ Món ăn:</h4>
                        <div>%s</div>

                        %s

                        <div class='next-step'>
                            <strong>Bước tiếp theo:</strong><br>
                            %s
                        </div>

                        <p style='margin-top:30px;'>Trân trọng,<br><em>CaterEase Team</em></p>
                    </div>
                </body>
                </html>
                """,
                booking.getName(),
                booking.getOrderCode(),
                statusColor,
                statusLabel,
                booking.getEventDate(),
                booking.getEventTime(),
                ev != null ? ev.getName() : "",
                venue != null ? venue.getName() : "",
                room != null ? room.getName() : "",
                menu != null ? menu.getName() : "",
                menu != null ? String.format("%,d", menu.getPrice()) : "0",
                dishHtmlList,
                (serviceNames.isEmpty() ? "" : "<h4 style='margin-top:20px;'>⭐ Dịch vụ kèm theo:</h4><div>" + serviceNames + "</div>"),
                nextStepMessage
        );
    }

    public static String generateBookingStatusUpdateEmail(
            Booking booking,
            String newStatus,
            Event ev,
            Venue venue,
            Room room,
            Menu menu,
            List<Dish> dishes,
            List<Service> services
    ) {
        String serviceNames = services.stream()
                .map(Service::getName)
                .collect(Collectors.joining(", "));

        String dishHtmlList = dishes.stream()
                .map(dish -> "<span style='display:inline-block;background:#fff;padding:4px 8px;margin:2px;border-radius:4px;font-size:14px;'>"
                        + dish.getName() + "</span>")
                .collect(Collectors.joining());

        String statusColor = "#6b7280";
        String statusLabel = "🕐 Chờ xử lý";
        String updateMessage = "Trạng thái đơn hàng đã được cập nhật.";

        if (newStatus != null) {
            switch (newStatus.toLowerCase()) {
                case "pending":
                    statusColor = "#f59e0b";
                    statusLabel = "🕐 Chờ xử lý";
                    updateMessage = "Đơn hàng của bạn đang trong trạng thái chờ xử lý.";
                    break;
                case "confirmed":
                    statusColor = "#3b82f6";
                    statusLabel = "✅ Đã xác nhận";
                    updateMessage = "Đơn hàng của bạn đã được xác nhận. Vui lòng chuẩn bị thanh toán.";
                    break;
                case "paid":
                    statusColor = "#10b981";
                    statusLabel = "💰 Đã thanh toán";
                    updateMessage = "Chúng tôi đã nhận được thanh toán. Cảm ơn bạn!";
                    break;
                case "cancelled":
                    statusColor = "#ef4444";
                    statusLabel = "❌ Đã hủy";
                    updateMessage = "Rất tiếc, đơn hàng của bạn đã bị hủy.";
                    break;
            }
        }

        return String.format("""
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset='utf-8'>
                    <style>
                        body { font-family:Segoe UI,Tahoma,Geneva,Verdana,sans-serif; background:#f4f4f4; color:#333; }
                        .container { max-width:600px;margin:20px auto;background:white;padding:30px;border-radius:12px;box-shadow:0 4px 6px rgba(0,0,0,0.1); }
                        .header { background:#10b981;color:white;padding:20px;border-radius:8px 8px 0 0;text-align:center; }
                        .info-row { display:flex;justify-content:space-between;padding:6px 0;border-bottom:1px solid #eee; }
                        .info-label { font-weight:bold; }
                        .info-value { text-align:right; }
                        .note { background:#e7f3ff;padding:15px;border-left:4px solid #0056b3;margin-top:20px;border-radius:4px; }
                    </style>
                </head>
                <body>
                    <div class='container'>
                        <div class='header'>
                            <h2>Cập nhật trạng thái đơn hàng</h2>
                        </div>
                        <p>Xin chào <strong>%s</strong>,</p>
                        <p>Trạng thái đơn hàng <strong>%s</strong> của bạn vừa được cập nhật:</p>

                        <div class='info-row'><span class='info-label'>Trạng thái mới:</span><span class='info-value' style='color:%s'>%s</span></div>
                        <div class='info-row'><span class='info-label'>Ngày tổ chức:</span><span class='info-value'>%s lúc %s</span></div>
                        <div class='info-row'><span class='info-label'>Sự kiện:</span><span class='info-value'>%s</span></div>
                        <div class='info-row'><span class='info-label'>Địa điểm:</span><span class='info-value'>%s - %s</span></div>
                        <div class='info-row'><span class='info-label'>Thực đơn:</span><span class='info-value'>%s (%s VND)</span></div>

                        <h4 style='margin-top:20px;'>🍽️ Món ăn:</h4>
                        <div>%s</div>

                        %s

                        <div class='note'>
                            <strong>Ghi chú:</strong><br>
                            %s
                        </div>

                        <p style='margin-top:30px;'>Trân trọng,<br><em>CaterEase Team</em></p>
                    </div>
                </body>
                </html>
                """,
                booking.getName(),
                booking.getOrderCode(),
                statusColor,
                statusLabel,
                booking.getEventDate(),
                booking.getEventTime(),
                ev != null ? ev.getName() : "",
                venue != null ? venue.getName() : "",
                room != null ? room.getName() : "",
                menu != null ? menu.getName() : "",
                menu != null ? String.format("%,d", menu.getPrice()) : "0",
                dishHtmlList,
                (serviceNames.isEmpty() ? "" : "<h4 style='margin-top:20px;'>⭐ Dịch vụ kèm theo:</h4><div>" + serviceNames + "</div>"),
                updateMessage
        );
    }
}

