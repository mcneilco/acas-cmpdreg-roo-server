package com.labsynch.cmpdreg.web;
import com.labsynch.cmpdreg.domain.SaltFormAliasKind;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/saltformaliaskinds")
@Controller
@RooWebScaffold(path = "saltformaliaskinds", formBackingObject = SaltFormAliasKind.class)
public class SaltFormAliasKindController {
}
