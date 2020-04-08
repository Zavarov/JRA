/*
 * Copyright (c) 2020 Zavarov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package vartas.reddit;

import java.util.Objects;

public abstract class Snowflake extends SnowflakeTOP {
    @Override
    public boolean equals(Object o){
        if(o instanceof SnowflakeTOP){
            SnowflakeTOP snowflake = (SnowflakeTOP)o;
            //We assume IDs to be unique
            return snowflake.getId().equals(this.getId());
        }else{
            return false;
        }
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(getId());
    }
}
