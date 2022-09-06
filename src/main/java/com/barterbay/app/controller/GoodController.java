package com.barterbay.app.controller;

import com.barterbay.app.domain.dto.good.CreateGoodDTO;
import com.barterbay.app.domain.dto.good.GoodDTO;
import com.barterbay.app.servcie.GoodService;
import com.barterbay.app.specification.SearchCriteria;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/good")
public class GoodController {

  private final GoodService goodService;

  @GetMapping("/{goodId}")
  public ResponseEntity<GoodDTO> getGoodById(@PathVariable long goodId) {
    return ResponseEntity.ok(goodService.findGoodById(goodId));
  }

  @GetMapping
  public ResponseEntity<List<GoodDTO>> findAllGoods() {
    return ResponseEntity.ok(goodService.findAllGoods());
  }

  @PutMapping(value = "/{goodId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  @ResponseStatus(HttpStatus.OK)
  public void updateGoodById(@PathVariable long goodId,
                             @ModelAttribute @Valid CreateGoodDTO goodDTO) {
    goodService.updateGoodById(goodId, goodDTO);
  }

  @DeleteMapping("/{goodId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteById(@PathVariable long goodId) {
    goodService.deleteGoodById(goodId);
  }

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  @ResponseStatus(HttpStatus.CREATED)
  public void createGood(@ModelAttribute @Valid CreateGoodDTO goodDTO) {
    goodService.saveGood(goodDTO);
  }

  @GetMapping("/search")
  @ResponseBody
  public ResponseEntity<List<GoodDTO>> searchGoods(@RequestBody SearchCriteria searchCriteria) {
    return ResponseEntity.ok(goodService.searchGoods(searchCriteria));
  }
}


