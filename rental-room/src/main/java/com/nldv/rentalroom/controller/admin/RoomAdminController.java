package com.nldv.rentalroom.controller.admin;

import com.nldv.rentalroom.enums.RoomStatus;
import com.nldv.rentalroom.pojo.Room;
import com.nldv.rentalroom.service.AreaService;
import com.nldv.rentalroom.service.RoomService;
import com.nldv.rentalroom.service.RoomTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/rooms")
public class RoomAdminController {

    private final RoomService roomService;
    private final RoomTypeService roomTypeService;
    private final AreaService areaService;

    public RoomAdminController(
            RoomService roomService,
            RoomTypeService roomTypeService,
            AreaService areaService) {

        this.roomService = roomService;
        this.roomTypeService = roomTypeService;
        this.areaService = areaService;
    }

    @GetMapping
    public String listRooms(Model model) {

        model.addAttribute("rooms", roomService.findAll());

        return "admin/rooms/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {

        model.addAttribute("room", new Room());

        model.addAttribute(
                "roomTypes",
                roomTypeService.findAll()
        );

        model.addAttribute(
                "areas",
                areaService.findAll()
        );

        model.addAttribute(
                "roomStatuses",
                RoomStatus.values()
        );

        return "admin/rooms/create";
    }

    @PostMapping
    public String createRoom(@ModelAttribute("room") Room room, RedirectAttributes redirectA) {

        roomService.save(room);
        redirectA.addFlashAttribute("successMessage", "Thêm phòng thành công");

        return "redirect:/admin/rooms";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(
            @PathVariable("id") Integer id,
            Model model) {

        Room room = roomService.findById(id);

        if (room == null) {
            return "redirect:/admin/rooms";
        }

        model.addAttribute("room", room);
        model.addAttribute("roomTypes", roomTypeService.findAll());
        model.addAttribute("areas", areaService.findAll());
        model.addAttribute("roomStatuses", RoomStatus.values());

        return "admin/rooms/edit";
    }

    @PostMapping("/update/{id}")
    public String updateRoom(
            @PathVariable("id") Integer id,
            @ModelAttribute("room") Room room,
            RedirectAttributes redirectAttributes) {

        Room existingRoom = roomService.findById(id);

        if (existingRoom == null) {
            return "redirect:/admin/rooms";
        }

        existingRoom.setRoomNumber(room.getRoomNumber());
        existingRoom.setTitle(room.getTitle());
        existingRoom.setDescription(room.getDescription());
        existingRoom.setAddress(room.getAddress());
        existingRoom.setAreaSize(room.getAreaSize());
        existingRoom.setPrice(room.getPrice());
        existingRoom.setStatus(room.getStatus());
        existingRoom.setRoomType(room.getRoomType());
        existingRoom.setArea(room.getArea());

        roomService.save(existingRoom);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Cập nhật phòng thành công!"
        );

        return "redirect:/admin/rooms";
    }

    @GetMapping("/delete/{id}")
    public String deleteRoom(
            @PathVariable("id") Integer id,
            RedirectAttributes redirectAttributes) {

        Room room = roomService.findById(id);

        if (room != null) {
            roomService.deleteById(id);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Xóa phòng thành công!"
            );
        }

        return "redirect:/admin/rooms";
    }
}
