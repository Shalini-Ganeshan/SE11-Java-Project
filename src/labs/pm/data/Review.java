/*
 * Copyright (C) 2024 ADMIN
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
package labs.pm.data;

/**
 *
 * @author ADMIN
 */
public class Review implements Comparable<Review> {

    @Override
    public int compareTo(Review o) {
        return o.rating.ordinal() - this.rating.ordinal();
    }
    private Rating rating;
    private String comment;

    public Review(Rating rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }

    public Rating getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "Review{" + "rating=" + rating + ", comment=" + comment + '}';
    }

}
