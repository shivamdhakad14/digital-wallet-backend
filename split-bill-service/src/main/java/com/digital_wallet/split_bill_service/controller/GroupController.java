package com.digital_wallet.split_bill_service.controller;

import com.digital_wallet.split_bill_service.dto.GroupDetailDto;
import com.digital_wallet.split_bill_service.dto.GroupDto;
import com.digital_wallet.split_bill_service.dto.GroupMemberDto;
import com.digital_wallet.split_bill_service.dto.GroupResponseDto;
import com.digital_wallet.split_bill_service.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/split-bill/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createGroup(@RequestBody GroupDto groupDto) {
        System.out.println("createGroup called : " + groupDto);
        try {
            groupService.createGroup(groupDto);

            Map<String, String> response = new HashMap<>();
            response.put("message", "group create successful");

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {

            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @GetMapping("/{userId}")
    public ResponseEntity<List<GroupResponseDto>> getAllGroupUserExist(@PathVariable Long userId){
        return ResponseEntity.ok(groupService.getAllGroupListUserExistOn(userId));
    }

    @GetMapping("/members/{groupId}")
    public ResponseEntity<List<GroupMemberDto>> getAllGroupMemberByGroupId(@PathVariable Long groupId){
        return ResponseEntity.ok(groupService.getAllGroupMember(groupId));
    }

    @GetMapping("/list/{groupId}")
    public ResponseEntity<GroupDto> getAllGroupUserExistByGroupId(@PathVariable Long groupId){
        return ResponseEntity.ok(groupService.getGroupById(groupId));
    }

    @GetMapping("/all-detail")
    public ResponseEntity<GroupDetailDto> getAllGroupDetail(@RequestParam Long groupId, @RequestParam Long userId){
        return ResponseEntity.ok(groupService.getAllDetailGroupById(groupId, userId));
    }
}
