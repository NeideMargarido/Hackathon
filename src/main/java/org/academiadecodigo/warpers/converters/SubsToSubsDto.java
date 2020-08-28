package org.academiadecodigo.warpers.converters;

import org.academiadecodigo.warpers.command.SubsDto;
import org.academiadecodigo.warpers.persistence.model.Subs;
import org.springframework.stereotype.Component;

@Component
public class SubsToSubsDto extends AbstractConverter<Subs, SubsDto>{


    @Override
    public SubsDto convert(Subs subs) {

        SubsDto subsDto = new SubsDto();

        subsDto.setSubsType(subs.getSubsType());
        subsDto.setMaxNumber(subs.getMaxMembers());

        return subsDto;
    }
}
